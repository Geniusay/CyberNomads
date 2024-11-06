package io.github.geniusay.core.supertask.plugin.terminator;

import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.pojo.DO.TaskDO;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CooldownTerminator extends AbstractTerminator {

    private static final long MIN_COOLDOWN_TIME = 10 * 1000L; // 10秒

    // 存储工作者ID和他们的下次执行任务的时间戳
    private final Map<Long, Long> workerCooldownMap = new ConcurrentHashMap<>();

    public CooldownTerminator(TaskDO taskDO, Map<String, Object> params) {
        super(taskDO, params);
    }

    @Override
    public boolean doTask(RobotWorker worker) {
        long currentTime = System.currentTimeMillis();
        long workerId = worker.getId();

        // 获取该工作者的下次允许执行任务的时间戳
        Long nextAvailableTime = workerCooldownMap.get(workerId);

        // 如果当前时间大于等于下次允许执行任务的时间，则允许执行任务
        if (nextAvailableTime == null || currentTime >= nextAvailableTime) {
            // 更新下次允许执行任务的时间戳
            workerCooldownMap.put(workerId, currentTime + MIN_COOLDOWN_TIME);
            return true;
        }

        // 冷却时间未到，不允许执行任务
        return false;
    }

    @Override
    public boolean taskIsDone() {
        return false;
    }
}