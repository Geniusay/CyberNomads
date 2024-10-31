package io.github.geniusay.core.task.enums;

/**
 * 描述: 任务状态枚举
 * @author suifeng
 * 日期: 2024/10/30
 */
public enum TaskStatus {
    PENDING,                // 未开始
    IN_PROGRESS,            // 进行中
    COMPLETED,              // 已完成
    PARTIALLY_COMPLETED,    // 部分完成
    FAILED,                 // 失败
    PAUSED                  // 暂停
}