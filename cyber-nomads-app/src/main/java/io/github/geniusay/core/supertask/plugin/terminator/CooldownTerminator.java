package io.github.geniusay.core.supertask.plugin.terminator;

import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import io.github.geniusay.pojo.DO.TaskDO;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.geniusay.constants.TerminatorConstants.*;


@Scope("prototype")
@Component(COOL_DOWN_TYPE_TIMES)
public class CooldownTerminator extends AbstractTerminator {

    // 默认冷却时间（10秒）
    private static final long MIN_COOLDOWN_TIME = 10 * 1000L;

    // 存储工作者ID和他们的下次允许执行任务的时间戳
    private final Map<Long, Long> workerCooldownMap = new ConcurrentHashMap<>();

    // 冷却时间，单位：毫秒
    private long cooldownTime;

    @Override
    public void init(Task task) {
        super.init(task);
        this.cooldownTime = Math.max(getParam(PARAM_COOLDOWN_TIME, Long.class), 10L) * 1000L;
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

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return List.of(
                TaskNeedParams.ofKV(PARAM_COOLDOWN_TIME,10L,"工作者冷却时间（秒）")
        );
    }
}
