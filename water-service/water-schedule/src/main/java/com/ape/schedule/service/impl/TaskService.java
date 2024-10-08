package com.ape.schedule.service.impl;

import com.ape.model.schedule.dtos.Task;

public interface TaskService {
    public long addTask(Task task);

    public boolean cancelTask(long taskId);

    //按照类型和优先级来拉取任务
    public  Task pull(int type,int priority);

    //任务调度
    public void refresh();

    //定时同步
    public void  reloadData();
}
