package io.github.geniusay.task.executor;

import io.github.geniusay.task.enums.TaskStatus;
import io.github.geniusay.task.po.SimpleTask;
import io.github.geniusay.task.po.Task;

/**
 * 描述: 简单任务执行器，返回任务是否成功执行
 * @author suifeng
 * 日期: 2024/10/30
 */
public abstract class SimpleTaskExecutor implements TaskExecutor<Boolean> {

    @Override
    public Boolean execute(Task task) {
        if (!(task instanceof SimpleTask)) {
            throw new IllegalArgumentException("Task is not a SimpleTask");
        }

        SimpleTask simpleTask = (SimpleTask) task;
        System.out.println("Executing simple task (" + simpleTask.getType() + ") with ID: " + simpleTask.getId() + " by founder: " + simpleTask.getFounderId());

        // 调用子类实现的具体简单任务执行逻辑
        int result = executeSimpleTask(simpleTask);

        // 根据子类返回的结果，判断任务是否成功
        if (result == 1) {
            simpleTask.setStatus(TaskStatus.COMPLETED);
            System.out.println("Simple task " + simpleTask.getId() + " completed.\n");
            return true;
        } else {
            simpleTask.setStatus(TaskStatus.FAILED);
            System.out.println("Simple task " + simpleTask.getId() + " failed.\n");
            return false;
        }
    }

    // 由子类去实现具体的简单任务执行逻辑
    public abstract int executeSimpleTask(SimpleTask simpleTask);
}