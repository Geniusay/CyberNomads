package io.github.geniusay.schedule.task;

import io.github.geniusay.core.supertask.task.Task;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/13 17:07
 */
public interface TaskSelector {

    Task select(Long robotId);

}
