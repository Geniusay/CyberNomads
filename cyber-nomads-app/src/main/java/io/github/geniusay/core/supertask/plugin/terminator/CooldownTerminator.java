package io.github.geniusay.core.supertask.plugin.terminator;

import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import io.github.geniusay.pojo.DO.TaskDO;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.geniusay.constants.TerminatorConstants.*;

public class CooldownTerminator extends AbstractTerminator {

    // 默认冷却时间（10秒）
    private static final long MIN_COOLDOWN_TIME = 10 * 1000L;

    // 存储工作者ID和他们的下次允许执行任务的时间戳
    private final Map<Long, Long> workerCooldownMap = new ConcurrentHashMap<>();

    // 冷却时间，单位：毫秒
    private final long cooldownTime;

    public CooldownTerminator(TaskDO taskDO, Map<String, Object> params) {
        super(taskDO, params);
        int cooldownInSeconds = getParam(PARAM_COOLDOWN_TIME, Integer.class);
        this.cooldownTime = Math.max(cooldownInSeconds, 10L) * 1000L;
    }

    @Override
    public boolean doTask(RobotWorker worker) {
        long currentTime = System.currentTimeMillis();
        long workerId = worker.getId();

        // 获取该工作者的下次允许执行任务的时间戳
        Long nextAvailableTime = workerCooldownMap.get(workerId);

        if (nextAvailableTime != null && currentTime < nextAvailableTime) {
            return true;
        }

        workerCooldownMap.put(workerId, currentTime + cooldownTime);
        return false;
    }


    @Override
    public boolean taskIsDone() {
        return false;
    }

    /**
     * 返回 CooldownTerminator 所需的参数
     */
    public static TaskNeedParams getTerminatorParams() {
        // 定义 cooldownTime 参数
        TaskNeedParams cooldownTimeParam = new TaskNeedParams(
                PARAM_COOLDOWN_TIME,
                Long.class,
                "工作者冷却时间（秒）",
                true,
                10L
        );

        // 返回 CooldownTerminator 的参数结构
        return new TaskNeedParams(
                COOL_DOWN_TYPE_TIMES,
                "无限火力终结器",
                true,
                null,
                List.of(cooldownTimeParam)  
        );
    }
}