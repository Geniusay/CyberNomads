package io.github.geniusay.task.po;

import io.github.geniusay.task.executor.TaskExecutor;
import io.github.geniusay.task.enums.Platform;
import io.github.geniusay.task.enums.TaskType;
import lombok.Data;

/**
 * 描述: 同项批量任务类 (表示多个相同的简单任务)
 * @author suifeng
 * 日期: 2024/10/30
 */
@Data
public class BatchTask extends SimpleTask {

    private int quantity;           // 总任务数
    private int completedCount;     // 已完成的任务数

    public BatchTask(String founderId, TaskType type, Platform platform, int quantity, TaskExecutor<?> executor) {
        super(founderId, type, platform, executor);
        this.quantity = quantity;
        this.completedCount = 0;    // 初始化已完成任务数为0
    }

    // 获取剩余任务数
    public int getRemainingTasks() {
        return quantity - completedCount;
    }

    // 增加已完成任务数
    public void incrementCompletedCount() {
        this.completedCount++;
    }

    @Override
    public int getCompletionDegree() {
        // 计算完成度，已完成数量/总数量 * 100
        return (completedCount * 100) / quantity;
    }
}