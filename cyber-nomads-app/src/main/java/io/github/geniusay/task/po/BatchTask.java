package io.github.geniusay.task.po;

import io.github.geniusay.task.enums.Platform;
import io.github.geniusay.task.enums.TaskType;

/**
 * 描述: 同项批量任务类 (表示多个相同的简单任务)
 * @author suifeng
 * 日期: 2024/10/30
 */
public abstract class BatchTask extends SimpleTask {

    private int quantity;           // 总任务数
    private int completedCount;     // 已完成的任务数

    public BatchTask(TaskType type, Platform platform, int quantity, int priority) {
        super(type, platform, priority);
        this.quantity = quantity;
        this.completedCount = 0;    // 初始化已完成任务数为0
    }

    @Override
    public int getCompletionDegree() {
        // 计算完成度，已完成数量/总数量 * 100
        return (completedCount * 100) / quantity;
    }
}