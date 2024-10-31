package io.github.geniusay.core.task.executor;

import io.github.geniusay.core.task.enums.TaskStatus;
import io.github.geniusay.core.task.po.CompositeTask;
import io.github.geniusay.core.task.po.Task;

/**
 * 描述: 复杂任务执行器，执行优先级最高的任务，并将其移入已执行的任务集合
 * @author suifeng
 * 日期: 2024/10/30
 */
public class CompositeTaskExecutor implements TaskExecutor<Boolean> {

    private volatile boolean paused = false; // 标志任务是否暂停
    private volatile Thread taskThread; // 保存任务执行线程

    @Override
    public Boolean execute(Task task) {
        if (!(task instanceof CompositeTask)) {
            throw new IllegalArgumentException("Task is not a CompositeTask");
        }

        CompositeTask compositeTask = (CompositeTask) task;
        compositeTask.setStatus(TaskStatus.IN_PROGRESS); // 设置状态为进行中
        System.out.println("Executing composite task with ID: " + compositeTask.getId() + " by founder: " + compositeTask.getFounderId());

        // 不断从未执行的任务中选取优先级最高的任务执行
        while (!compositeTask.getPendingTasks().isEmpty()) {

            // 检查任务是否暂停
            if (paused) {
                compositeTask.setStatus(TaskStatus.PAUSED);
                System.out.println("Composite task " + compositeTask.getId() + " is paused.");
                return false; // 暂停任务执行
            }

            // 选取优先级最高的任务
            Task highestPriorityTask = compositeTask.getPendingTasks().peek(); // 使用 peek() 获取队首元素

            System.out.println("Executing sub-task with ID: " + highestPriorityTask.getId() + " with priority: " + highestPriorityTask.getPriority());

            // 执行该任务
            Boolean result = highestPriorityTask.execute();

            // 无论成功或失败，都将任务移入已完成集合
            compositeTask.moveTaskToDone(highestPriorityTask);

            // 如果任务执行失败，记录失败并继续执行剩余任务
            if (!result) {
                System.out.println("Sub-task with ID: " + highestPriorityTask.getId() + " failed.");
                highestPriorityTask.setStatus(TaskStatus.FAILED);
            } else {
                System.out.println("Sub-task with ID: " + highestPriorityTask.getId() + " completed.");
                highestPriorityTask.setStatus(TaskStatus.COMPLETED);
            }
        }

        // 如果所有任务执行完毕，设置状态为完成
        if (compositeTask.getDoneTasks().stream().allMatch(t -> t.getStatus() == TaskStatus.COMPLETED)) {
            compositeTask.setStatus(TaskStatus.COMPLETED);
            System.out.println("Composite task " + compositeTask.getId() + " completed.");
        } else {
            compositeTask.setStatus(TaskStatus.PARTIALLY_COMPLETED);
            System.out.println("Composite task " + compositeTask.getId() + " partially completed.");
        }

        return true;
    }

    // 暂停任务
    public void pause() {
        this.paused = true;
        if (taskThread != null) {
            taskThread.interrupt(); // 中断当前执行线程
        }
    }

    // 恢复任务
    public void resume(Task task) {
        this.paused = false;
        CompositeTask compositeTask = (CompositeTask) task;
        compositeTask.setStatus(TaskStatus.IN_PROGRESS); // 恢复后重置状态为进行中
        taskThread = new Thread(() -> execute(task)); // 创建新的线程继续执行任务
        taskThread.start();
    }
}
