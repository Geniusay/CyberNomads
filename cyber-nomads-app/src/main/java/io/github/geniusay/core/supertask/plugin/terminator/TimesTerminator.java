package io.github.geniusay.core.supertask.plugin.terminator;

import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import io.github.geniusay.pojo.DO.RobotDO;
import io.github.geniusay.pojo.DO.TaskDO;
import io.github.geniusay.constants.TerminatorConstants;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static io.github.geniusay.constants.TerminatorConstants.PARAM_TIMES;
import static io.github.geniusay.constants.TerminatorConstants.TERMINATOR_TYPE_TIMES;

/**
 * 任务计数器终止器
 * ept：一个robot可以做{times}次任务
 */
public class TimesTerminator extends AbstractTerminator {

    private final int singleTimes;
    private final Map<Long, AtomicInteger> robotDoneTimes;

    public TimesTerminator(TaskDO taskDO, Map<String, Object> params) {
        super(taskDO, params);
        // 从 params 中提取 times 参数
        this.singleTimes = getParam(PARAM_TIMES, Integer.class);
        this.robotDoneTimes = new ConcurrentHashMap<>();
        for (RobotDO robotDo : robotList) {
            robotDoneTimes.put(robotDo.getId(), new AtomicInteger(0));
        }
    }

    @Override
    public boolean doTask(RobotWorker worker) {
        return robotDoneTimes.get(worker.getId()).incrementAndGet() <= singleTimes;
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

    /**
     * 返回 TimesTerminator 所需的参数
     */
    public static TaskNeedParams getTerminatorParams() {
        TaskNeedParams singleTimesParam = new TaskNeedParams(
                PARAM_TIMES,
                Integer.class,
                "每个机器人可以执行的任务次数",
                true,
                5
        );

        return new TaskNeedParams(
                TERMINATOR_TYPE_TIMES,
                "计数终结器参数",
                true,
                List.of(singleTimesParam)
        );
    }
}