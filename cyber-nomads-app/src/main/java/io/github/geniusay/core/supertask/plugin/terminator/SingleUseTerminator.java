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

import static io.github.geniusay.constants.TerminatorConstants.*;

@Slf4j
@Scope("prototype")
@Component(SINGLE_USE)
public class SingleUseTerminator extends AbstractTerminator {

    // 存储任务的冷却时间（单位：毫秒）
    private long cooldownTime;

    // 目标数（用户指定的目标数，可能会被调整为工作者数量）
    private int targetCount;

    private final AtomicInteger completedCount = new AtomicInteger(0);
    private final AtomicLong nextAvailableTime = new AtomicLong(0);
    private final Map<Long, Boolean> workerDoneMap = new ConcurrentHashMap<>();

    @Override
    public void init(Task task) {
        super.init(task);

        this.targetCount = getParam(PARAM_TARGET_COUNT, Integer.class);
        this.cooldownTime = getParam(PARAM_COOLDOWN_TIME, Long.class) * 1000L;  // 转换为毫秒
        int workerCount = task.getRobots().size();

        if (targetCount > workerCount) {
            log.info("用户指定的目标数 {} 大于工作者数量 {}，将目标数调整为 {}", targetCount, workerCount, workerCount);
            this.targetCount = workerCount;
        }
        log.info("初始化单次终结器，目标数: {}，冷却时间: {} 秒", this.targetCount, this.cooldownTime / 1000);
    }

    @Override
    public boolean robotCanDo(RobotWorker worker) {
        long currentTime = System.currentTimeMillis();

        long temp = nextAvailableTime.get();

        if (currentTime < temp || workerDoneMap.getOrDefault(worker.getId(), false)) {
            return false;
        }

        return nextAvailableTime.compareAndSet(temp, currentTime + cooldownTime);
    }

    @Override
    public void doTask(RobotWorker worker) {
        workerDoneMap.put(worker.getId(), true);
        int currentCount = completedCount.incrementAndGet();
        log.info("工作者 {} 完成任务，当前已完成任务数: {}, 任务进入冷却状态，冷却时间: {} 秒", worker.getId(), currentCount, cooldownTime / 1000);
    }

    @Override
    public boolean taskIsDone() {
        return completedCount.get() >= targetCount;
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return List.of(
                TaskNeedParams.ofKV(PARAM_TARGET_COUNT, 5, "任务目标数，填写数量不能超过工作者数量"),
                TaskNeedParams.ofKV(PARAM_COOLDOWN_TIME, 10L, "任务冷却时间（秒）")
        );
    }
}