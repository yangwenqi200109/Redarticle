package com.ape.waterwemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.ape.apis.schedule.IScheduleClient;
import com.ape.model.common.dtos.ResponseResult;
import com.ape.model.common.enums.TaskTypeEnum;
import com.ape.model.schedule.dtos.Task;
import com.ape.model.wemedia.pojos.WmNews;
import com.ape.utils.common.ProtostuffUtil;
import com.ape.waterwemedia.service.WmNewsTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class WmNewsTaskServiceImpl implements WmNewsTaskService {
    @Autowired
    private IScheduleClient scheduleClient;

    /**
     * 添加任务到延迟队列中
     *
     * @param id          文章的id
     * @param publishTime 发布的时间 可以做为任务的执行时间
     */
    @Override
    @Async
    public void addNewsToTask(Integer id, Date publishTime) {
        log.info("添加任务到延迟服务中----begin");
        Task task = new Task();
        task.setExecuteTime(publishTime.getTime());
        task.setTaskType(TaskTypeEnum.NEWS_SCAN_TIME.getTaskType());
        task.setPriority(TaskTypeEnum.NEWS_SCAN_TIME.getPriority());
        WmNews wmNews = new WmNews();
        wmNews.setId(id);
        task.setParameters(ProtostuffUtil.serialize(wmNews));
        scheduleClient.addTask(task);
        log.info("添加任务到延迟服务中----end");
    }

    @Autowired
    private WmNewsAutoScanServiceImpl wmNewsAutoScanService;

    /**
     * 消费延迟队列数据
     */
    @Scheduled(fixedRate = 1000)
    @Override
    public void scanNewsByTask() {
        log.info("文章审核---消费任务执行---begin---");
        ResponseResult responseResult = scheduleClient.pull(TaskTypeEnum.NEWS_SCAN_TIME.getTaskType(), TaskTypeEnum.NEWS_SCAN_TIME.getPriority());
        if (responseResult.getCode().equals(200) && responseResult.getData() != null) {
            String json_str = JSON.toJSONString(responseResult.getData());
            Task task = JSON.parseObject(json_str, Task.class);
            byte[] parameters = task.getParameters();
            WmNews wmNews = ProtostuffUtil.deserialize(parameters, WmNews.class);
            System.out.println(wmNews.getId() + "-----------");
            wmNewsAutoScanService.autoScanWmNews(wmNews.getId());
        }
        log.info("文章审核---消费任务执行---end---");
    }
}
