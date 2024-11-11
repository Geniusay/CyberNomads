package io.github.geniusay.core.supertask.plugin.terminator;

import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import io.github.geniusay.pojo.DO.RobotDO;
import io.github.geniusay.pojo.DO.TaskDO;
import io.github.geniusay.constants.TerminatorConstants;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static io.github.geniusay.constants.TerminatorConstants.*;
import static io.github.geniusay.core.supertask.config.PluginConstant.TERMINATOR_GROUP_NAME;

/**
 * 任务计数器终止器
 * ept：一个robot可以做{times}次任务
 */
@Scope("prototype")
@Component(TERMINATOR_TYPE_TIMES)
public class TimesTerminator extends AbstractTerminator {

    private int singleTimes;
    private Map<Long, AtomicInteger> robotDoneTimes;


    @Override
    public void init(Task task) {
        super.init(task);
        this.singleTimes = getParam(PARAM_TIMES, Integer.class);
        this.robotDoneTimes = new ConcurrentHashMap<>();
        for (RobotDO robotDo : task.getRobots()) {
            robotDoneTimes.put(robotDo.getId(), new AtomicInteger(singleTimes));
        }
    }

    @Override
    public boolean robotCanDo(RobotWorker worker) {
        return robotDoneTimes.get(worker.getId()).get() > 0;
    }

    @Override
    public void doTask(RobotWorker worker) {
        robotDoneTimes.get(worker.getId()).decrementAndGet();
    }

    @Override
    public boolean taskIsDone() {
        for (AtomicInteger count : robotDoneTimes.values()) {
            if (count.get() < singleTimes) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return List.of(
                TaskNeedParams.ofKV(PARAM_TIMES, 5, "游民执行次数")
        );
    }
}
