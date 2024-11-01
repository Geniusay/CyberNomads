package io.github.geniusay.schedule.strategy;

import io.github.geniusay.core.supertask.task.Task;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/2 0:35
 */
@Component
public class FreeRobotStrategy implements ScheduleStrategy{
    @Override
    public Object execute(Task task) {
        return null;
    }
}
