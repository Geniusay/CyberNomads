package io.github.geniusay.task.factory;

import io.github.geniusay.task.executor.TaskExecutor;
import io.github.geniusay.task.enums.TaskType;
import io.github.geniusay.task.po.BatchTask;
import io.github.geniusay.task.po.CompositeTask;
import io.github.geniusay.task.po.SimpleTask;

/**
 * 描述: 任务工厂接口
 * 日期: 2024/10/30
 */
public interface TaskFactory {

    /**
     * 创建简单任务
     */
    SimpleTask createSimpleTask(String founderId, TaskType type, TaskExecutor<?> executor);

    /**
     * 创建批量任务
     */
    BatchTask createBatchTask(String founderId, TaskType type, int quantity, TaskExecutor<?> executor);

    /**
     * 创建复杂任务
     */
    default CompositeTask createCompositeTask(String founderId, TaskExecutor<?> executor) {
        return new CompositeTask(founderId, executor);
    }
}