package io.github.geniusay.schedule.strategy;

import io.github.geniusay.constants.TaskActionConstant;
import io.github.geniusay.core.event.EventManager;
import io.github.geniusay.core.event.commonEvent.AfterTaskExecuteEvent;
import io.github.geniusay.core.event.commonEvent.TaskStatusEditEvent;
import io.github.geniusay.core.supertask.config.TaskStatus;
import io.github.geniusay.core.supertask.task.RobotWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.Executor;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/13 18:04
 */
@Component
@Slf4j
public class SyncTaskExecute implements  TaskExecuteStrategy{
    @Autowired
    @Qualifier("taskExecutor")
    private Executor executor;
    @Resource
    EventManager eventManager;
    @Override
    public void execute(RobotWorker worker) {
        executor.execute(
                ()->{
                    try {
                        worker.execute();
                    } catch (Exception e) {
                        log.error("robot执行异常:{},robot信息:{}", e.getMessage()+":"+e.getStackTrace()[0],worker.getId());
                        eventManager.publishEvent(new TaskStatusEditEvent(worker.task().getId(),TaskActionConstant.EXCEPTION));
                    } finally {
                        String lastTalk = worker.task().getLastWord().lastTalk(worker);
                        String taskId = worker.task().getId();
                        eventManager.publishEvent(new AfterTaskExecuteEvent(worker,taskId,lastTalk));
                    }
                }
        );
    }
}
