package com.ape.schedule.test;

import com.ape.model.schedule.dtos.Task;
import com.ape.schedule.ScheduleApplication;;
import com.ape.schedule.service.impl.TaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Date;

@SpringBootTest(classes = ScheduleApplication.class)
@RunWith(SpringRunner.class)
public class TaskServiceImplTest {

    @Autowired
    private TaskService taskService;

    @Test
    public void addTask() {

        //当前时间
//        Task task = new Task();
//        task.setTaskType(100);
//        task.setPriority(50);
//        task.setParameters("task test".getBytes());
//        task.setExecuteTime(new Date().getTime());

//        未来时间
        Task task = new Task();
        task.setTaskType(100);
        task.setPriority(50);
        task.setParameters("task test".getBytes());
        task.setExecuteTime(new Date().getTime()+40000);

        //未来时间超过5分钟任务
//        Task task = new Task();
//        task.setTaskType(100);
//        task.setPriority(50);
//        task.setParameters("task test".getBytes());
//        task.setExecuteTime(new Date().getTime()+500000);


        long taskId = taskService.addTask(task);
        System.out.println(taskId);
    }

    @Test
    public void cancelTesk(){
        taskService.cancelTask(1393402270461292545L);
    }

    @Test
    public void testpull(){
        Task task = taskService.pull(100, 50);
        System.out.println(task);
    }

    @Test
    public void ref(){
        taskService.refresh();
    }

    @Test
    public void reloadDataTest(){
        taskService.reloadData();
    }
}