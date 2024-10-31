package io.github.geniusay.core.task.po;

import io.github.geniusay.core.task.enums.TaskStatus;
import io.github.geniusay.core.task.executor.TaskExecutor;
import io.github.geniusay.utils.RandomUtil;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述: 任务基类
 * @author suifeng
 * 日期: 2024/10/30
 */
@Data
public abstract class Task {

    private String id;                      // 任务Id
    private String founderId;               // 创建者Id
    private int level;                      // 任务级别 (0:简单任务; >0:复杂任务)
    private int priority;                   // 任务优先级
    private TaskStatus status;              // 任务状态

    private Map<String, Object> params;     // 任务执行参数
    private TaskExecutor<?> executor;       // 任务执行逻辑

    private LocalDateTime createdTime;      // 任务创建时间
    private LocalDateTime startTime;        // 任务开始时间
    private LocalDateTime endTime;          // 任务结束时间

    public Task(String founderId, int level, TaskExecutor<?> executor) {
        this.id = generateId();             // 生成唯一任务ID
        this.founderId = founderId;
        this.level = level;
        this.executor = executor;
        this.priority = 0;                  // 初始化为0
        this.status = TaskStatus.PENDING;   // 初始化为未开始
        this.createdTime = LocalDateTime.now(); // 在创建时记录当前时间
        this.params = new HashMap<>();      // 初始化参数Map
    }

    // 添加任务参数
    public void addParam(String key, Object value) {
        params.put(key, value);
    }

    // 取得任务参数
    public Object getParam(String key) {
        return params.get(key);
    }

    // 执行任务并返回执行结果
    public <R> R execute() {
        this.startTime = LocalDateTime.now(); // 记录任务开始时间
        R result = null;
        if (executor != null) {
            result = (R) executor.execute(this);
        }
        this.endTime = LocalDateTime.now();   // 记录任务结束时间
        return result;
    }

    // 抽象方法：计算任务的完成度
    public abstract int getCompletionDegree();

    // 生成唯一任务ID (使用UUID)
    private String generateId() {
        return RandomUtil.generateId();
    }
}
