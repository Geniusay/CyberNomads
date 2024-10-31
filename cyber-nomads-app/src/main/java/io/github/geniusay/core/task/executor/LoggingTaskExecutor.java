package io.github.geniusay.core.task.executor;
import io.github.geniusay.core.task.po.Task;

/**
 * 描述: 日志记录包装执行器，记录任务执行过程中的日志
 * @author suifeng
 * 日期: 2024/10/30
 */
public class LoggingTaskExecutor<R> implements TaskExecutor<R> {

    private final TaskExecutor<R> wrappedExecutor;

    public LoggingTaskExecutor(TaskExecutor<R> wrappedExecutor) {
        this.wrappedExecutor = wrappedExecutor;
    }

    @Override
    public R execute(Task task) {
        System.out.println("Starting execution of task with ID: " + task.getId() + " by founder: " + task.getFounderId());

        // 调用实际的执行逻辑并获取执行结果
        R result = wrappedExecutor.execute(task);

        System.out.println("Finished execution of task with ID: " + task.getId() + " with status: " + task.getStatus());
        return result;
    }
}
