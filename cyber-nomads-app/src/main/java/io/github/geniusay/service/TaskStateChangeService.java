package io.github.geniusay.service;

import io.github.geniusay.core.supertask.config.TaskStatus;
import io.github.geniusay.pojo.DO.TaskDO;

public interface TaskStateChangeService {

    /**
     * 通知调度器任务已删除
     *
     * @param task      任务对象
     * @param oldStatus 修改前的状态
     */
    void notifyTaskDeleted(TaskDO task, TaskStatus oldStatus);

    /**
     * 通知调度器任务已重置
     *
     * @param task      任务对象
     * @param oldStatus 修改前的状态
     * @param newStatus 修改后的状态
     */
    void notifyTaskReset(TaskDO task, TaskStatus oldStatus, TaskStatus newStatus);

    /**
     * 通知调度器任务已开始
     *
     * @param task      任务对象
     * @param oldStatus 修改前的状态
     * @param newStatus 修改后的状态
     */
    void notifyTaskStarted(TaskDO task, TaskStatus oldStatus, TaskStatus newStatus);

    void notifyTaskRegister(TaskDO taskDO);
}
