package io.github.geniusay.task.po;

import io.github.geniusay.task.enums.TaskStatus;
import lombok.Data;

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

    private int level;                      // 任务级别 (0:简单任务; >0:复杂任务)

    private int priority;                   // 任务优先级

    private TaskStatus status;              // 任务状态

    private Map<String, Object> params;     // 任务执行参数

    public Task(int level, int priority) {
        this.level = level;
        this.priority = priority;
    }

    // 添加任务参数
    public void addParam(String key, Object value) {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put(key, value);
    }

    // 取得任务参数
    public Object getParams(String key) {
        if (params == null) {
            return null;
        }
        return params.get(key);
    }

    // 抽象方法：执行任务
    public abstract void execute();

    // 抽象方法：计算任务的完成度
    public abstract int getCompletionDegree();
}
