package io.github.geniusay.core.supertask.plugin.terminator;

import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import io.github.geniusay.pojo.DO.TaskDO;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 任务计数器终止器
 * ept：一个robot可以做{times}次任务
 */
public abstract class TimesTerminator extends AbstractTerminator {

    private final int times;
    private final Map<Long, AtomicInteger> robotDoneTimes;

    public TimesTerminator(TaskDO taskDO, List<RobotWorker> robotWorkers) {
        super(taskDO);
        // 从 taskDO 的 params 中提取 times 参数
        this.times = getParam("times", Integer.class);
        this.robotDoneTimes = new ConcurrentHashMap<>();
        for (RobotWorker robotWorker : robotWorkers) {
            robotDoneTimes.put(robotWorker.getId(), new AtomicInteger(0));
        }
    }

    @Override
    public boolean doTask(RobotWorker worker) {
        return robotDoneTimes.get(worker.getId()).incrementAndGet() <= times;
    }

    @Override
    public boolean taskIsDone() {
        // 这里可以根据逻辑判断任务是否完成
        return false;
    }
}
