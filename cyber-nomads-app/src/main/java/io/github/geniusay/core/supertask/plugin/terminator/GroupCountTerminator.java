package io.github.geniusay.core.supertask.plugin.terminator;

import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.TaskNeedParams;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 总计数终结器
 * ept：任务一共要完成多少次{targetCount}
 */
public class GroupCountTerminator implements Terminator {

    private final int targetCount;

    private final AtomicInteger nowCount;

    public GroupCountTerminator(int targetCount, AtomicInteger nowCount) {
        this.targetCount = targetCount;
        this.nowCount = nowCount;
    }

    @Override
    public boolean doTask(RobotWorker worker) {
        return nowCount.incrementAndGet() <= targetCount;
    }

    @Override
    public boolean taskIsDone() {
        return targetCount<=nowCount.get();
    }

    @Override
    public List<TaskNeedParams> pluginParams() {
        return null;
    }
}
