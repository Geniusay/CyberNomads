package io.github.geniusay.schedule.task;

import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.schedule.TaskScheduleManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/13 17:22
 */
@Component
@Slf4j
public class FirstTaskSelector implements TaskSelector {
    @Resource
    TaskScheduleManager manager;
    @Override
    public Task select(Long robotId) {
        Map<String, Task> taskMap = manager.getRobotTaskById(robotId);
        RobotWorker worker = manager.getRobotById(robotId);
        if(worker==null){
            return null;
        }
        List<Task> tasks = new ArrayList<>(taskMap.values());
        for (Task task : tasks) {
            if (task.getTerminator().robotCanDo(worker)) {
                return task;
            }
        }
        return null;
    }
}
