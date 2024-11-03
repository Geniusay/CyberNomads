package io.github.geniusay.service.Impl;

import io.github.geniusay.core.supertask.config.TaskStatus;
import io.github.geniusay.pojo.DO.TaskDO;
import io.github.geniusay.service.TaskStateChangeService;
import org.springframework.stereotype.Service;

@Service
public class ITaskStateChangeService implements TaskStateChangeService {

    @Override
    public void notifyTaskDeleted(TaskDO task, TaskStatus oldStatus) {
        System.out.println("任务已删除: " + task.getTaskName() + "，原状态: " + oldStatus);
    }

    @Override
    public void notifyTaskReset(TaskDO task, TaskStatus oldStatus, TaskStatus newStatus) {
        System.out.println("任务已重置: " + task.getTaskName() + "，从 " + oldStatus + " 修改为 " + newStatus);
    }

    @Override
    public void notifyTaskStarted(TaskDO task, TaskStatus oldStatus, TaskStatus newStatus) {
        System.out.println("任务已开始: " + task.getTaskName() + "，从 " + oldStatus + " 修改为 " + newStatus);
    }
}