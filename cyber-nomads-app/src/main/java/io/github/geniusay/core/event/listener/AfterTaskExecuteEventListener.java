package io.github.geniusay.core.event.listener;

import io.github.geniusay.constants.TaskActionConstant;
import io.github.geniusay.core.event.Event;
import io.github.geniusay.core.event.EventListener;
import io.github.geniusay.core.event.EventManager;
import io.github.geniusay.core.event.commonEvent.AfterTaskExecuteEvent;
import io.github.geniusay.core.event.commonEvent.TaskStatusEditEvent;
import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.schedule.TaskScheduleManager;
import io.github.geniusay.schedule.WorkerExecute;
import io.github.geniusay.service.TaskLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/13 18:14
 */
@Component
@Slf4j
public class AfterTaskExecuteEventListener implements EventListener {

    private TaskLogService logService;
    private TaskScheduleManager manager;
    private WorkerExecute workerExecute;
    private TaskStatusEditEventListener listener;

    @Lazy
    @Resource
    public void setLogService(TaskLogService logService) {
        this.logService = logService;
    }

    @Lazy
    @Resource
    public void setManager(TaskScheduleManager manager) {
        this.manager = manager;
    }

    @Lazy
    @Resource
    public void setWorkerExecute(WorkerExecute workerExecute) {
        this.workerExecute = workerExecute;
    }

    @Lazy
    @Resource
    public void setListener(TaskStatusEditEventListener listener) {
        this.listener = listener;
    }

    @Override
    public void pushEvent(Event event) {
        if(event instanceof AfterTaskExecuteEvent){
            log.info("任务执行后事件发送.......");
            RobotWorker worker = ((AfterTaskExecuteEvent) event).getWorker();
            String lastTalk = ((AfterTaskExecuteEvent) event).getLastTalk();
            String taskId = ((AfterTaskExecuteEvent) event).getTaskId();
            logService.logTaskResult(worker, lastTalk);
            if(worker.task().getTerminator().taskIsDone()){
                listener.pushEvent(new TaskStatusEditEvent(taskId, TaskActionConstant.FINISH));
                manager.removeWorkerTask(taskId);
            }
            workerExecute.taskDoneCallBack(worker.getId(),taskId);
        }
    }
}
