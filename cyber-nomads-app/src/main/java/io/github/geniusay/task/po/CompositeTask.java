package io.github.geniusay.task.po;

import io.github.geniusay.task.executor.TaskExecutor;
import lombok.Data;

import java.util.*;

/**
 * 描述: 复杂任务类 (由简单任务或者复杂任务组合得到)
 * @author suifeng
 * 日期: 2024/10/30
 */
@Data
public class CompositeTask extends Task {

    private PriorityQueue<Task> pendingTasks;  // 未执行的子任务集合 (有序)
    private List<Task> doneTasks;              // 已执行的子任务集合

    public CompositeTask(String founderId, TaskExecutor<?> executor) {
        super(founderId, 1, executor);         // 复杂任务的级别至少为1
        // 使用 PriorityQueue 存储未执行的任务，并按优先级排序
        this.pendingTasks = new PriorityQueue<>(new TaskComparator());
        this.doneTasks = new ArrayList<>();
    }

    // 添加子任务到未执行的集合
    public void addSubTask(Task task) {
        pendingTasks.add(task);
        // 动态调整复杂任务的级别，取子任务中最大的级别+1
        this.setLevel(pendingTasks.stream().mapToInt(Task::getLevel).max().orElse(0) + 1);
    }

    @Override
    public int getCompletionDegree() {
        int totalTasks = pendingTasks.size() + doneTasks.size();
        if (totalTasks == 0) {
            return 0;
        }
        // 计算所有已完成任务的平均完成度
        int totalCompletion = doneTasks.stream().mapToInt(Task::getCompletionDegree).sum();
        return totalCompletion / totalTasks;
    }

    // 移动任务到已完成集合
    public void moveTaskToDone(Task task) {
        doneTasks.add(task);
        pendingTasks.remove(task);
    }

    // 自定义比较器，按优先级排序
    private static class TaskComparator implements Comparator<Task> {
        @Override
        public int compare(Task t1, Task t2) {
            // 先比较优先级，优先级高的排在前面
            return Integer.compare(t2.getPriority(), t1.getPriority());
        }
    }
}