package io.github.geniusay.schedule.strategy;

import io.github.geniusay.core.supertask.task.RobotWorker;

/**
 * @Description 同步
 * @Author welsir
 * @Date 2024/11/13 18:03
 */
public interface TaskExecuteStrategy {

    void execute(RobotWorker worker);
}
