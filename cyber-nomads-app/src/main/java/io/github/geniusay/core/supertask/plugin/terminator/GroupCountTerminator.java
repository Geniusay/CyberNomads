package io.github.geniusay.core.supertask.plugin.terminator;

import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.Task;
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
    public void init(Task task) {
        super.init(task);
        this.targetCount = getParam(PARAM_TARGET_COUNT, Integer.class);
        this.nowCount = new AtomicInteger(targetCount);
    }

    @Override
    public boolean robotCanDo(RobotWorker worker) {
        return  nowCount.get() > 0;
    }

    @Override
    public void doTask(RobotWorker worker) {
        nowCount.decrementAndGet();
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
}
