package io.github.geniusay.core.event.listener;

import io.github.geniusay.core.event.Event;
import io.github.geniusay.core.event.EventListener;
import io.github.geniusay.core.event.commonEvent.TaskStatusEditEvent;
import io.github.geniusay.service.Impl.TaskStatusManager;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/13 18:21
 */
@Component
public class TaskStatusEditEventListener implements EventListener {

    @Resource
    TaskStatusManager taskStatusManager;

    @Override
    public void pushEvent(Event event) {
        if(event instanceof TaskStatusEditEvent){
            String status = ((TaskStatusEditEvent) event).getStatus();
            String taskId = ((TaskStatusEditEvent) event).getTaskId();
            taskStatusManager.modifyTask(Long.valueOf(taskId),status);
        }
    }
}
