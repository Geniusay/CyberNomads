package io.github.geniusay.task.executor;

import io.github.geniusay.task.po.Task;

/**
 * 描述: 任务执行函数式接口，用于传递任务的执行逻辑，并返回执行结果
 * @author suifeng
 * 日期: 2024/10/30
 */
@FunctionalInterface
public interface TaskExecutor<R> {
    R execute(Task task);
}