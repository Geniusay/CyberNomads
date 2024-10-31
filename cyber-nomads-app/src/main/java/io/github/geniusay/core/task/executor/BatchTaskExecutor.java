package io.github.geniusay.core.task.executor;

import io.github.geniusay.core.task.enums.TaskStatus;
import io.github.geniusay.core.task.po.BatchTask;
import io.github.geniusay.core.task.po.SimpleTask;
import io.github.geniusay.core.task.po.Task;

/**
 * 描述: 批量任务执行器，处理多个相同的简单任务，返回任务是否成功执行
 * @author suifeng
 * 日期: 2024/10/30
 */
public class BatchTaskExecutor implements TaskExecutor<Boolean> {

    private final SimpleTaskExecutor simpleTaskExecutor;
    private volatile boolean paused = false; // 标志任务是否暂停
    private volatile Thread taskThread; // 保存任务执行线程

    public BatchTaskExecutor(SimpleTaskExecutor simpleTaskExecutor) {
        this.simpleTaskExecutor = simpleTaskExecutor;
    }

    @Override
    public Boolean execute(Task task) {
        if (!(task instanceof BatchTask)) {
            throw new IllegalArgumentException("Task is not a BatchTask");
        }

        BatchTask batchTask = (BatchTask) task;
        batchTask.setStatus(TaskStatus.IN_PROGRESS); // 设置状态为进行中
        System.out.println("Executing batch task with ID: " + batchTask.getId() + " by founder: " + batchTask.getFounderId());

        int failedCount = 0; // 记录失败任务的数量

        // 模拟任务执行的逻辑
        while (batchTask.getRemainingTasks() > 0) {

            // 检查任务是否暂停
            if (paused) {
                batchTask.setStatus(TaskStatus.PAUSED);
                System.out.println("Batch task " + batchTask.getId() + " is paused.");
                return false; // 暂停任务执行
            }

            // 创建一个简单任务，并传递params
            SimpleTask simpleTask = new SimpleTask(
                    batchTask.getFounderId(),
                    batchTask.getType(),
                    batchTask.getPlatform(),
                    simpleTaskExecutor
            );

            // 将批量任务的参数传递给简单任务
            simpleTask.setParams(batchTask.getParams());

            // 执行简单任务
            Boolean result = simpleTask.execute();

            // 如果某个简单任务失败，记录失败日志，但不减少剩余任务数
            if (!result) {
                failedCount++;
                System.out.println("Simple task failed: " + simpleTask.getId() + ". Logging failure.");
                logFailure(simpleTask); // 记录失败日志
            } else {
                // 任务成功，减少剩余任务数
                batchTask.incrementCompletedCount();
                System.out.println("Batch task progress: " + batchTask.getCompletedCount() + "/" + batchTask.getQuantity());
            }
        }

        // 如果所有任务执行完毕，设置状态为完成
        if (failedCount == 0) {
            batchTask.setStatus(TaskStatus.COMPLETED);
            System.out.println("Batch task " + batchTask.getId() + " completed with no failures.");
        } else {
            batchTask.setStatus(TaskStatus.PARTIALLY_COMPLETED); // 新增部分完成状态
            System.out.println("Batch task " + batchTask.getId() + " completed with " + failedCount + " failures.");
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
        BatchTask batchTask = (BatchTask) task;
        batchTask.setStatus(TaskStatus.IN_PROGRESS); // 恢复后重置状态为进行中
        taskThread = new Thread(() -> execute(task)); // 创建新的线程继续执行任务
        taskThread.start();
    }

    // 记录失败任务的日志
    private void logFailure(SimpleTask simpleTask) {
        System.out.println("Logging failure for task ID: " + simpleTask.getId() + ", type: " + simpleTask.getType());
    }
}
