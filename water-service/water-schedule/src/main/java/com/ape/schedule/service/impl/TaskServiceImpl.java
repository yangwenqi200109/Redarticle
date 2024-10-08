package com.ape.schedule.service.impl;

import com.alibaba.fastjson.JSON;
import com.ape.common.constants.ScheduleConstants;
import com.ape.common.redis.CacheService;
import com.ape.model.schedule.dtos.Task;
import com.ape.model.schedule.pojos.Taskinfo;
import com.ape.model.schedule.pojos.TaskinfoLogs;
import com.ape.schedule.mapper.TaskinfoLogsMapper;
import com.ape.schedule.mapper.TaskinfoMapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@Transactional
public class TaskServiceImpl implements TaskService {
    //添加任务
    @Override
    public long addTask(Task task) {
        //1.添加任务到数据库中
        boolean success = addTaskToDb(task);
        if (success) {
            //2.添加任务到redis
            addTaskToCache(task);
        }
        return task.getTaskId();
    }

    //取消任务
    @Override
    public boolean cancelTask(long taskId) {
        boolean flag = false;
        //删除任务，更新日志
        Task task = updateDb(taskId, ScheduleConstants.EXECUTED);
        //删除redis的数据
        if (task != null) {
            removeTaskFromCache(task);
            flag = true;
        }
        return flag;
    }

    // 按照类型和优先级拉取任务
    @Override
    public Task pull(int type, int priority) {
        Task task = null;
        try {
            String key = type + "_" + priority;
            String task_json = cacheService.lRightPop(ScheduleConstants.TOPIC + key);
            if(StringUtils.isNotBlank(task_json)) {
                task = JSON.parseObject(task_json, Task.class);
                //更新数据库信息
                updateDb(task.getTaskId(), ScheduleConstants.EXECUTED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("pool task exception");
        }
        return task;
    }

    @Scheduled(cron = "0 */1 * * * ?")
    public void refresh() {
        String token = cacheService.tryLock("FUTURE_TASK_SYNC", 1000 * 30);
        if (StringUtils.isNotBlank(token)) {
            log.info("未来数据定时刷新 -- 定时任务");
            //获取所有未来的数据集合key值
            Set<String> futurekeys = cacheService.scan(ScheduleConstants.FUTURE + "*");//future_*
            for (String futurekey : futurekeys) {
                String topickey = ScheduleConstants.TOPIC + futurekey.split(ScheduleConstants.FUTURE)[1];
                //获取该组key下当前需要消费的任务数据
                Set<String> tasks = cacheService.zRangeByScore(futurekey, 0, System.currentTimeMillis());
                if (!tasks.isEmpty()) {
                    //将这些任务数据添加到消费者队列中
                    cacheService.refreshWithPipeline(futurekey, topickey, tasks);
                    System.out.println("成功将" + futurekey + "下的数据刷新到" + topickey + "下");
                }
            }
        }
    }

    //定时同步
    @Scheduled(cron = "0 */5 * * * ?")
    @PostConstruct
    public void reloadData() {
        clearCache();
        log.info("数据库数据同步到缓存");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);
        //查看未来小于5分钟的所有任务
        List<Taskinfo> allTasks = taskinfoMapper.selectList(Wrappers.<Taskinfo>lambdaQuery().lt(Taskinfo::getExcuteTime, calendar.getTime()));
        if (allTasks != null && allTasks.size() > 0) {
            for (Taskinfo taskinfo : allTasks) {
                Task task = new Task();
                BeanUtils.copyProperties(taskinfo, task);
                task.setExecuteTime(taskinfo.getExcuteTime().getTime());
                addTaskToCache(task);
            }
        }
    }

    //删除缓存中未来数据集合和当前消费者队列的所有key
    public void clearCache() {
        Set<String> futurekeys = cacheService.scan(ScheduleConstants.FUTURE + "*");
        Set<String> topickeys = cacheService.scan(ScheduleConstants.TOPIC + "*");
        cacheService.delete(futurekeys);
        cacheService.delete(topickeys);
    }


    //删除redis中的任务数据
    private void removeTaskFromCache(Task task) {
        String key = task.getTaskType() + "_" + task.getPriority();
        if (task.getExecuteTime() <= System.currentTimeMillis()) {
            cacheService.lRemove(ScheduleConstants.TOPIC + key, 0, JSON.toJSONString(task));
        } else {
            cacheService.zRemove(ScheduleConstants.FUTURE + key, JSON.toJSONString(task));
        }
    }

    //删除任务，更新任务状态
    private Task updateDb(long taskId, int status) {
        Task task = null;
        try {
            //删除任务
            taskinfoMapper.deleteById(taskId);

            TaskinfoLogs taskinfoLogs = taskinfoLogsMapper.selectById(taskId);
            taskinfoLogs.setStatus(status);
            taskinfoLogsMapper.updateById(taskinfoLogs);

            task = new Task();
            BeanUtils.copyProperties(taskinfoLogs, task);
            task.setExecuteTime(taskinfoLogs.getExcuteTime().getTime());
        } catch (Exception e) {
            log.error("task cancel exception taskid = {}", taskId);
        }
        return task;
    }


    @Autowired
    private CacheService cacheService;

    //把任务添加到redis中
    private void addTaskToCache(Task task) {
        String key = task.getTaskType() + "_" + task.getPriority();
        //获取五分钟之后的时间 毫秒值
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);
        long nextSchedultTime = calendar.getTimeInMillis();
        //2.1 如果任务的执行时间小于等于当前时间，存入list
        if (task.getExecuteTime() <= System.currentTimeMillis()) {
            cacheService.lLeftPush(ScheduleConstants.TOPIC + key, JSON.toJSONString(task));
        } else if (task.getExecuteTime() <= nextSchedultTime) {
            //2.2 如果任务的执行时间大于当前的和时间 && 小于等于预设时间（未来五分钟） 存入zset中
            cacheService.zAdd(ScheduleConstants.FUTURE + key, JSON.toJSONString(task), task.getExecuteTime());
        }
    }

    @Autowired
    private TaskinfoMapper taskinfoMapper;
    @Autowired
    private TaskinfoLogsMapper taskinfoLogsMapper;

    //添加任务到数据库中
    private boolean addTaskToDb(Task task) {
        boolean flag = false;
        try {
            Taskinfo taskinfo = new Taskinfo();
            BeanUtils.copyProperties(task, taskinfo);
            taskinfo.setExcuteTime(new Date(task.getExecuteTime()));
            taskinfoMapper.insert(taskinfo);
            //设置taskID
            task.setTaskId(taskinfo.getTaskId());
            //保存任务日志数据
            TaskinfoLogs taskinfoLogs = new TaskinfoLogs();
            BeanUtils.copyProperties(taskinfo, taskinfoLogs);
            taskinfoLogs.setVersion(1);
            taskinfoLogs.setStatus(ScheduleConstants.SCHEDULED);
            taskinfoLogsMapper.insert(taskinfoLogs);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
}
