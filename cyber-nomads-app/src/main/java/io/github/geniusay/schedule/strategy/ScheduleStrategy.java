package io.github.geniusay.schedule.strategy;

import io.github.geniusay.core.supertask.task.Task;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/2 0:34
 */
public interface ScheduleStrategy {

    Object execute(Task task);

}
