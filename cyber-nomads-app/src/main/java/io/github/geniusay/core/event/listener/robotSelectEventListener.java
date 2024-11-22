package io.github.geniusay.core.event.listener;

import io.github.geniusay.core.event.Event;
import io.github.geniusay.core.event.EventListener;
import io.github.geniusay.core.event.commonEvent.robotSelecteEvent;
import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.schedule.TaskScheduleManager;
import io.github.geniusay.schedule.WorkerExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/13 17:42
 */
@Component
@Slf4j
public class robotSelectEventListener implements EventListener {
    @Resource
    TaskScheduleManager manager;
    @Resource
    WorkerExecutor workerExecutor;
    @Override
    public void pushEvent(Event event) {
        if (event instanceof robotSelecteEvent) {
            robotSelecteEvent selectEvent = (robotSelecteEvent) event;
            RobotWorker worker = manager.getRobotById(selectEvent.getRobotId());
            if(worker==null){
                return;
            }
            workerExecutor.executeTask(worker);
        }
    }
}
