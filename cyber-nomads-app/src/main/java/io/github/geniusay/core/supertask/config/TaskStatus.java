package io.github.geniusay.core.supertask.config;

/**
 * 描述: 任务状态枚举
 * @author suifeng
 * 日期: 2024/10/30
 */
public enum TaskStatus {
    PENDING,                // 未开始
    RUNNING,                // 进行中
    EXCEPTION,              // 进行中
    FAILED,                 // 失败
    PAUSED,                 // 暂停
    COMPLETED               // 已完成
}
