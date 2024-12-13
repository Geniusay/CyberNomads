package io.github.geniusay.core.event.listener;

import io.github.geniusay.core.event.Event;
import io.github.geniusay.core.event.EventListener;
import io.github.geniusay.core.event.commonEvent.CancelSharedTaskEvent;
import io.github.geniusay.schedule.TaskScheduleManager;

import javax.annotation.Resource;

public class CancelSharedTaskEventListener implements EventListener {
    @Resource
    private TaskScheduleManager scheduleManager;

    @Override
    public void pushEvent(Event event) {
        if(event instanceof CancelSharedTaskEvent){
            CancelSharedTaskEvent sharedTaskEvent = (CancelSharedTaskEvent) event;
            Long robotId = sharedTaskEvent.getRobotId();
            String uid = sharedTaskEvent.getUid();
            scheduleManager.removeTaskExceptUid(robotId, uid);
        }
    }
}
