package io.github.geniusay.core.task.po;

import io.github.geniusay.core.supertask.config.TaskStatus;
import io.github.geniusay.core.task.enums.TaskType;
import io.github.geniusay.core.task.executor.TaskExecutor;
import io.github.geniusay.core.task.enums.Platform;
import lombok.Data;

/**
 * 描述: 简单任务类 (不可拆分)
 * @author suifeng
 * 日期: 2024/10/30
 */
@Data
public class SimpleTask extends Task {

    private TaskType type;          // 任务类型
    private Platform platform;      // 任务所属平台

    public SimpleTask(String founderId, TaskType type, Platform platform, TaskExecutor<?> executor) {
        super(founderId, 0, executor);         // 简单任务的级别固定为0
        this.type = type;
        this.platform = platform;
    }

    @Override
    public int getCompletionDegree() {
        // 简单任务的完成度要么是0（未完成），要么是100（已完成）
        return this.getStatus() == TaskStatus.COMPLETED ? 100 : 0;
    }
}
