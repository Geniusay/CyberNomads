package io.github.geniusay.core.event.listener;

import io.github.geniusay.core.event.Event;
import io.github.geniusay.core.event.EventListener;
import io.github.geniusay.core.event.commonEvent.UnsharedRobotEvent;
import io.github.geniusay.schedule.TaskScheduleManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description
 * @Author welsir
 * @Date 2024/12/13 22:06
 */
@Component
@Slf4j
public class UnsharedRobotEventListener implements EventListener{

    @Resource
    TaskScheduleManager manager;

    @Override
    public void pushEvent(Event event) {
        if(event instanceof UnsharedRobotEvent){
            manager.unshared(((UnsharedRobotEvent) event).getRobotId());
        }
    }
}
