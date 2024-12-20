package io.github.geniusay.schedule;

import io.github.geniusay.core.supertask.task.Task;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/1 23:26
 */
public interface TaskListener {

    void registerRobotWorker(Long robotId);
    void removeTask(Task task);
    void registerRobot(Long robotId);
    void removeRobot(Long robotId);

    void initRobot();
}
