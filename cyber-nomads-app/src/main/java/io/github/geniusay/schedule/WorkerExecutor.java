package io.github.geniusay.schedule;

import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.schedule.storage.WorkerStorage;
import io.github.geniusay.schedule.strategy.TaskExecuteStrategy;
import io.github.geniusay.schedule.task.TaskSelector;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/13 14:46
 */
@Component
public class WorkerExecutor implements WorkerExecute {
    @Resource
    WorkerStorage workerStorage;
    @Resource
    TaskSelector taskSelector;
    @Resource
    TaskExecuteStrategy executeStrategy;

    @Override
    public void push(Long robotId){
        workerStorage.joinRobotWorker(robotId);
    }

    @Override
    public void taskDoneCallBack(Long workerId,String taskId) {

    }

    public void executeTask(RobotWorker worker){
        if(worker==null){
            return;
        }
        Task currentTask = taskSelector.select(worker.getId());
        if(currentTask==null){
            if(!taskSelector.taskIsFinish(worker.getId())){
                workerStorage.reJoinRobotWorker(worker.getId());
            }
            return;
        }
        worker.setTask(currentTask);
        executeStrategy.execute(worker);
    }
}
