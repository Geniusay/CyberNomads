package io.github.geniusay.core.supertask.plugin.terminator;

import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static io.github.geniusay.constants.PluginConstant.SINGLE_PARAM_COOLDOWN_TIME_EXT_DESC;
import static io.github.geniusay.constants.TerminatorConstants.*;

@Slf4j
@Scope("prototype")
@Component(SINGLE_USE)
public class SingleUseTerminator extends AbstractTerminator {

    // 默认冷却时间
    private static final long DEFAULT_COOLDOWN_TIME_SECONDS = 5L;

    // 存储任务的冷却时间（单位：毫秒）
    private long cooldownTime;

    // 目标数（由机器人数量决定）
    private int targetCount;

    private final AtomicInteger completedCount = new AtomicInteger(0);
    private final AtomicLong nextAvailableTime = new AtomicLong(0);
    private final Map<Long, Boolean> workerDoneMap = new ConcurrentHashMap<>();

    @Override
    public void init(Task task) {
        super.init(task);
        this.targetCount = task.getRobots().size();
        this.cooldownTime = Math.max(getParam(PARAM_COOLDOWN_TIME, Long.class), DEFAULT_COOLDOWN_TIME_SECONDS) * 1000L;
        log.info("初始化单次终结器，机器人数量: {}，冷却时间: {} 秒", this.targetCount, this.cooldownTime / 1000);
    }

    @Override
    public boolean robotCanDo(RobotWorker worker) {
        long currentTime = System.currentTimeMillis();
        long availableTime = nextAvailableTime.get();

        // 检查冷却时间和该机器人是否已经完成任务
        if (currentTime < availableTime || workerDoneMap.getOrDefault(worker.getId(), false)) {
            return false;
        }

        // 使用 CAS 操作设置下次可用时间，确保线程安全
        return nextAvailableTime.compareAndSet(availableTime, currentTime + cooldownTime);
    }

    @Override
    public void doTask(RobotWorker worker) {
        workerDoneMap.put(worker.getId(), true);
        int currentCount = completedCount.incrementAndGet();

        log.info("工作者 {} 完成任务，当前已完成任务数: {}，任务进入冷却状态，冷却时间: {} 秒", worker.getId(), currentCount, cooldownTime / 1000);
    }

    @Override
    public boolean taskIsDone() {
        // 当已完成任务的计数达到目标数时，任务完成
        return completedCount.get() >= targetCount;
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return List.of(
                TaskNeedParams.ofKV(PARAM_COOLDOWN_TIME, DEFAULT_COOLDOWN_TIME_SECONDS, "冷却时间（秒），不得少于" + DEFAULT_COOLDOWN_TIME_SECONDS + "秒")
                        .setExtendDesc(SINGLE_PARAM_COOLDOWN_TIME_EXT_DESC)
        );
    }
}
