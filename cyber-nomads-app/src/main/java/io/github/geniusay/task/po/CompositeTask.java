package io.github.geniusay.task.po;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述: 复杂任务类 (由简单任务或者复杂任务组合得到)
 * @author suifeng
 * 日期: 2024/10/30
 */
public abstract class CompositeTask extends Task {

    private int degree;             // 完成度

    private List<Task> subTasks;    // 子任务集合

    public CompositeTask(int priority) {
        super(1, priority);         // 复杂任务的级别至少为1
        this.subTasks = new ArrayList<>();
    }

    // 添加子任务
    public void addSubTask(Task task) {
        subTasks.add(task);
        // 动态调整复杂任务的级别，取子任务中最大的级别+1
        this.setLevel(subTasks.stream().mapToInt(Task::getLevel).max().orElse(0) + 1);
    }

    @Override
    public int getCompletionDegree() {
        if (subTasks.isEmpty()) {
            return 0;
        }
        // 计算所有子任务的平均完成度
        int totalCompletion = subTasks.stream().mapToInt(Task::getCompletionDegree).sum();
        return totalCompletion / subTasks.size();
    }
}