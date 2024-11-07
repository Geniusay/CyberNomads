package io.github.geniusay.service;

import io.github.geniusay.core.supertask.task.RobotWorker;

/**
 * TaskLogService 接口，定义日志记录方法
 */
public interface TaskLogService {

    /**
     * 根据任务的遗言记录日志到数据库
     */
    void logTaskResult(RobotWorker robotWorker);
}