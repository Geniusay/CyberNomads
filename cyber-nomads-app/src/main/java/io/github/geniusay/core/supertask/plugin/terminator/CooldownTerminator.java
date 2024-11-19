package io.github.geniusay.core.supertask.plugin.terminator;

import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import io.github.geniusay.pojo.DO.TaskLogDO;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.geniusay.constants.PluginConstant.PARAM_COOLDOWN_TIME_EXT_DESC;
import static io.github.geniusay.constants.TerminatorConstants.*;


@Scope("prototype")
@Component(COOL_DOWN_TYPE_TIMES)
public class CooldownTerminator extends AbstractTerminator {

    // 默认冷却时间
    private static final long DEFAULT_COOLDOWN_TIME_SECONDS = 30L;

    // 存储工作者ID和他们的下次允许执行任务的时间戳
    private final Map<Long, Long> workerCooldownMap = new ConcurrentHashMap<>();

    // 冷却时间，单位：毫秒
    private long cooldownTime;

    // 标记任务是否失败
    private boolean taskFailed = false;

    @Override
    public void init(Task task) {
        super.init(task);
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

        // 每次执行任务后检查是否有连续失败的日志
        if (checkConsecutiveFailures(worker.task())) {
            taskFailed = true;
        }
    }

    /**
     * 检查任务日志是否有连续10条失败的记录
     */
    private boolean checkConsecutiveFailures(Task task) {
        List<TaskLogDO> logList = task.getLoglist();
        if (logList == null || logList.size() < 10) {
            return false;
        }

        int failureCount = 0;

        // 从最近的日志开始倒序检查
        for (int i = logList.size() - 1; i >= 0; i--) {
            TaskLogDO log = logList.get(i);

            if (!log.isSuccess()) {
                failureCount++;
            } else {
                failureCount = 0;
            }

            if (failureCount >= 10) {
                task.log("任务连续失败10次，标记任务为失败");
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean taskIsDone() {
        return taskFailed;
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        // 冷却时间至少为30秒
        return List.of(
                TaskNeedParams.ofKV(PARAM_COOLDOWN_TIME, DEFAULT_COOLDOWN_TIME_SECONDS, "冷却时间（秒），不得少于" + DEFAULT_COOLDOWN_TIME_SECONDS + "秒").setExtendDesc(PARAM_COOLDOWN_TIME_EXT_DESC)
        );
    }
}
