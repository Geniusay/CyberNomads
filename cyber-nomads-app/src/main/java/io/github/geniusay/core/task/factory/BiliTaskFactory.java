package io.github.geniusay.core.task.factory;

import io.github.geniusay.core.task.enums.TaskType;
import io.github.geniusay.core.task.executor.TaskExecutor;
import io.github.geniusay.core.task.enums.Platform;
import io.github.geniusay.core.task.po.BatchTask;
import io.github.geniusay.core.task.po.SimpleTask;

/**
 * 描述: b站任务工厂类
 * @author suifeng
 * 日期: 2024/10/30
 */
public class BiliTaskFactory implements TaskFactory {

    private static final Platform PLATFORM = Platform.BILI;

    @Override
    public SimpleTask createSimpleTask(String founderId, TaskType type, TaskExecutor<?> executor) {
        return new SimpleTask(founderId, type, PLATFORM, executor);
    }

    @Override
    public BatchTask createBatchTask(String founderId, TaskType type, int quantity, TaskExecutor<?> executor) {
        return new BatchTask(founderId, type, PLATFORM, quantity, executor);
    }
}
