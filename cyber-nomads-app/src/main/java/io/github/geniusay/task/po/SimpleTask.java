package io.github.geniusay.task.po;

import io.github.geniusay.task.enums.Platform;
import io.github.geniusay.task.enums.TaskStatus;
import io.github.geniusay.task.enums.TaskType;

/**
 * 描述: 简单任务类 (不可拆分)
 * @author suifeng
 * 日期: 2024/10/30
 */
public abstract class SimpleTask extends Task {

    private TaskType type;          // 任务类型

    private Platform platform;      // 任务所属平台

    public SimpleTask(TaskType type, Platform platform, int priority) {
        super(0, priority);         // 简单任务的级别固定为0
        this.type = type;
        this.platform = platform;
    }

    @Override
    public int getCompletionDegree() {
        // 简单任务的完成度要么是0（未完成），要么是100（已完成）
        return this.getStatus() == TaskStatus.COMPLETED ? 100 : 0;
    }
}