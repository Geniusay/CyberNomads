package io.github.geniusay.core.supertask.plugin.terminator;

import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import io.github.geniusay.pojo.DO.TaskDO;
import io.github.geniusay.constants.TerminatorConstants;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static io.github.geniusay.constants.TerminatorConstants.*;

/**
 * 总计数终结器
 * ept：任务一共要完成多少次{targetCount}
 */
@Scope("prototype")
@Component(TERMINATOR_TYPE_GROUP_COUNT)
public class GroupCountTerminator extends AbstractTerminator {

    private int targetCount;
    private AtomicInteger nowCount;


    @Override
    public void init(TaskDO taskDO, Map<String, Object> params) {
        super.init(taskDO, params);
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

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return List.of(
                TaskNeedParams.ofKV(PARAM_TARGET_COUNT,10,"总计执行次数")
        );
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
                null,
                List.of(targetCountParam)
        );
    }
}
