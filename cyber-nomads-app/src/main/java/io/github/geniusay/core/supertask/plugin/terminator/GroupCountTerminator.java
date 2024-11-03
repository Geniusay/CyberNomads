package io.github.geniusay.core.supertask.plugin.terminator;

import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import io.github.geniusay.pojo.DO.TaskDO;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 总计数终结器
 * ept：任务一共要完成多少次{targetCount}
 */
public abstract class GroupCountTerminator extends AbstractTerminator {

    private final int targetCount;
    private final AtomicInteger nowCount;

    public GroupCountTerminator(TaskDO taskDO) {
        super(taskDO);
        // 从 taskDO 的 params 中提取 targetCount 参数
        this.targetCount = getParam("targetCount", Integer.class);
        this.nowCount = new AtomicInteger(0);
    }

    @Override
    public boolean doTask(RobotWorker worker) {
        return nowCount.incrementAndGet() <= targetCount;
    }

    @Override
    public boolean taskIsDone() {
        return targetCount <= nowCount.get();
    }
}
