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

    // 默认冷却时间（30秒）
    private static final long DEFAULT_COOLDOWN_TIME_SECONDS = 30L;

    // 存储工作者ID和他们的下次允许执行任务的时间戳
    private final Map<Long, Long> workerCooldownMap = new ConcurrentHashMap<>();

    // 冷却时间，单位：毫秒
    private long cooldownTime;

    @Override
    public void init(Task task) {
        super.init(task);
        // 冷却时间不得少于30秒
        this.cooldownTime = Math.max(getParam(PARAM_COOLDOWN_TIME, Long.class), DEFAULT_COOLDOWN_TIME_SECONDS) * 1000L;
    }

    @Override
    public boolean robotCanDo(RobotWorker worker) {
        long currentTime = System.currentTimeMillis();
        // 获取该工作者的下次允许执行任务的时间戳
        Long nextAvailableTime = workerCooldownMap.get(worker.getId());

        if (nextAvailableTime != null && currentTime < nextAvailableTime) {
            return false;
        }
        return true;
    }

    @Override
    public void doTask(RobotWorker worker) {
        long currentTime = System.currentTimeMillis();
        long workerId = worker.getId();
        workerCooldownMap.put(workerId, currentTime + cooldownTime);
    }

    @Override
    public boolean taskIsDone() {
        return false;
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return List.of(
                TaskNeedParams.ofKV(PARAM_COOLDOWN_TIME, DEFAULT_COOLDOWN_TIME_SECONDS, "工作者冷却时间（秒），不得少于" + DEFAULT_COOLDOWN_TIME_SECONDS + "秒")
        );
    }
}
