package io.github.geniusay.service.Impl;

import io.github.geniusay.core.supertask.config.TaskStatus;
import io.github.geniusay.pojo.DO.TaskDO;
import io.github.geniusay.schedule.TaskScheduleManager;
import io.github.geniusay.service.TaskStateChangeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ITaskStateChangeService implements TaskStateChangeService {

    @Resource
    private TaskScheduleManager manager;

    @Override
    public void notifyTaskDeleted(TaskDO task, TaskStatus oldStatus) {
        manager.removeTask(task.getId());
    }

    @Override
    public void notifyTaskReset(TaskDO task, TaskStatus oldStatus, TaskStatus newStatus) {
        System.out.println("任务已重置: " + task.getTaskName() + "，从 " + oldStatus + " 修改为 " + newStatus);
    }

    @Override
    public void notifyTaskStarted(TaskDO task, TaskStatus oldStatus, TaskStatus newStatus) {
        manager.registerTaskAndStart(task);
        System.out.println("任务已开始: " + task.getTaskName() + "，从 " + oldStatus + " 修改为 " + newStatus);
    }

    @Override
    public void notifyTaskPaused(TaskDO task, TaskStatus oldStatus, TaskStatus newStatus) {
        // 这里可以实现暂停任务的逻辑
        System.out.println("任务已暂停: " + task.getTaskName() + "，从 " + oldStatus + " 修改为 " + newStatus);
    }

    @Override
    public void notifyTaskFinished(TaskDO task, TaskStatus oldStatus, TaskStatus newStatus) {
        // 这里可以实现完成任务的逻辑
        System.out.println("任务已完成: " + task.getTaskName() + "，从 " + oldStatus + " 修改为 " + newStatus);
    }

    @Override
    public void notifyTaskRegister(TaskDO taskDO) {
    }
}
