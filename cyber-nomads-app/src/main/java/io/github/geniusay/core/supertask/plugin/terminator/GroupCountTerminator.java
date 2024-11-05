package io.github.geniusay.core.supertask.plugin.terminator;

import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import io.github.geniusay.pojo.DO.TaskDO;
import io.github.geniusay.constants.TerminatorConstants;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static io.github.geniusay.constants.TerminatorConstants.PARAM_TARGET_COUNT;
import static io.github.geniusay.constants.TerminatorConstants.TERMINATOR_TYPE_GROUP_COUNT;

/**
 * 总计数终结器
 * ept：任务一共要完成多少次{targetCount}
 */
public class GroupCountTerminator extends AbstractTerminator {

    private final int targetCount;
    private final AtomicInteger nowCount;

    public GroupCountTerminator(TaskDO taskDO, Map<String, Object> params) {
        super(taskDO, params);
        // 从 params 中提取 targetCount 参数
        this.targetCount = getParam(PARAM_TARGET_COUNT, Integer.class);
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

    /**
     * 返回 GroupCountTerminator 所需的参数
     */
    public static TaskNeedParams getTerminatorParams() {
        TaskNeedParams targetCountParam = new TaskNeedParams(
                PARAM_TARGET_COUNT,
                Integer.class,
                "任务一共要完成多少次",
                true,
                10
        );

        return new TaskNeedParams(
                TERMINATOR_TYPE_GROUP_COUNT,
                "总计数终结器参数",
                true,
                List.of(targetCountParam)
        );
    }
}