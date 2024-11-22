package io.github.geniusay.core.event.listener;

import io.github.geniusay.core.event.Event;
import io.github.geniusay.core.event.EventListener;
import io.github.geniusay.core.event.commonEvent.RemoveWorkerEventIfNeed;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.schedule.TaskScheduleManager;
import io.github.geniusay.schedule.WorkerExecute;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/21 23:06
 */
@Component
public class RemoveWorkerEventIfNeedListener implements EventListener {
    @Resource
    TaskScheduleManager manager;
    @Resource
    WorkerExecute workerExecute;
    @Override
    public void pushEvent(Event event) {
        if(event instanceof RemoveWorkerEventIfNeed){
            String workerId = ((RemoveWorkerEventIfNeed) event).getWorkerId();
            Map<String, Task> taskMap = manager.getRobotTaskById(Long.valueOf(workerId));
            if (Objects.nonNull(taskMap) && !taskMap.isEmpty()) {
                workerExecute.push(Long.valueOf(workerId));
            }
        }
    }
}
