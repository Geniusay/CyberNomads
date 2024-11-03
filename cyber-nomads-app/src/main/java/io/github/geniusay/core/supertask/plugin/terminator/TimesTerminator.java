package io.github.geniusay.core.supertask.plugin.terminator;

import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.TaskNeedParams;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 任务计数器终止器
 * ept：一个robot可以做{times}次任务
 */
public class TimesTerminator implements Terminator {

    private final int times;

    private final Map<Long, AtomicInteger> robotDoneTimes;


    public TimesTerminator(int times, List<RobotWorker> robotWorkers) {
        this.times = times;
        robotDoneTimes = new ConcurrentHashMap<>();
        for (RobotWorker robotWorker : robotWorkers) {
            robotDoneTimes.put(robotWorker.getId(), new AtomicInteger(0));
        }
    }

    @Override
    public boolean doTask(RobotWorker worker) {
        return robotDoneTimes.get(worker.getId()).incrementAndGet() > times;
    }

    @Override
    public boolean taskIsDone() {

        return false;
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return null;
    }
}
