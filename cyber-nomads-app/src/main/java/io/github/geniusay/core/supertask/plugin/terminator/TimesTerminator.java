package io.github.geniusay.core.supertask.plugin.terminator;

import io.github.geniusay.core.supertask.task.RobotWorker;
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
    public void init(TaskDO taskDO, Map<String, Object> params) {
        super.init(taskDO, params);
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

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return List.of(
                TaskNeedParams.ofKV(PARAM_TIMES, 5, "游民执行次数")
        );
    }

    /**
     * 返回 TimesTerminator 所需的参数
     */
    public static TaskNeedParams getTerminatorParams() {
        // 定义 times 参数
        TaskNeedParams singleTimesParam = new TaskNeedParams(
                PARAM_TIMES,
                Integer.class,
                "每个机器人可以执行的任务次数",
                true,
                5
        );

        // 返回 TimesTerminator 的参数结构
        return new TaskNeedParams(
                TERMINATOR_TYPE_TIMES,
                "计数终结器参数",
                true,
                null,  // 没有 selection
                List.of(singleTimesParam)  // params 表示具体的参数
        );
    }
}
