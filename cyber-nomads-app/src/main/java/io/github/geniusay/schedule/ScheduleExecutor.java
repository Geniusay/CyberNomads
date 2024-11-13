package io.github.geniusay.schedule;

import io.github.geniusay.constants.TaskActionConstant;
import io.github.geniusay.core.supertask.config.TaskStatus;
import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.pojo.DO.RobotDO;
import io.github.geniusay.service.Impl.TaskStatusManager;
import io.github.geniusay.service.TaskLogService;
import io.github.geniusay.utils.LastWordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.*;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/31 21:36
 */
@Component
@Slf4j
@DependsOn("taskScheduleManager")
public class ScheduleExecutor implements TaskListener{
    @Resource
    private Executor taskExecutor;
    @Resource
    private TaskScheduleManager manager;
    @Resource
    TaskStatusManager taskStatusManager;
    @Resource
    private TaskLogService taskLogService;

    private final BlockingQueue<Long> FREE_WORKER = new LinkedBlockingQueue<>();
    private final ConcurrentHashMap<String, String> TASK_STATUS = new ConcurrentHashMap<>();

    public void mainThread(){
        while (true){
            try {
                Long robotId = FREE_WORKER.take();
                Map<String, Task> taskMap = manager.getRobotTaskById(robotId);
                log.info("robotId:{}",robotId);
                if(!Objects.isNull(taskMap)&&!taskMap.isEmpty()){
                    List<Task> tasks = new ArrayList<>(taskMap.values());
                    RobotWorker robotWorker = manager.getRobotById(robotId);
                    Task selectedTask = null;
                    for (Task task : tasks) {
                        if(task.getTerminator().robotCanDo(robotWorker)){
                            selectedTask = task;
                        }
                    }
                    if(selectedTask==null){
                        log.info("无可用任务");
                        FREE_WORKER.add(robotId);
                        continue;
                    }
                    robotWorker.setTask(selectedTask);
                    TASK_STATUS.putIfAbsent(selectedTask.getId(), TaskStatus.RUNNING.toString());
                    taskExecutor.execute(() -> {
                        try {
                            robotWorker.execute();
                        } catch (Exception e) {
                            log.error("robot执行异常:{},robot信息:{}", e.getMessage()+":"+e.getStackTrace()[0],robotWorker.getId());
                            taskStatusManager.modifyTask(Long.valueOf(robotWorker.task().getId()),TaskActionConstant.EXCEPTION);
                            TASK_STATUS.put(robotWorker.task().getId(),TaskStatus.EXCEPTION.toString());
                        } finally {
                            String lastTalk = robotWorker.task().getLastWord().lastTalk(robotWorker);
                            taskLogService.logTaskResult(robotWorker, lastTalk);

                            String taskId = robotWorker.task().getId();
                            if(robotWorker.task().getTerminator().taskIsDone() && canChangeTaskStatus(taskId)){
                                manager.removeWorldRobotTask(robotId,robotWorker.getCurrentTask().getId());
                                if(manager.getRobotTaskById(robotId).isEmpty()){
                                    manager.removeWorldRobot(robotId);
                                }
                                taskStatusManager.modifyTask(Long.valueOf(taskId), TaskActionConstant.FINISH);
                            }else{
                                FREE_WORKER.add(robotId);
                            }
                        }
                    });
                } else{
                    manager.removeWorldRobot(robotId);
                }
            } catch (Exception e) {
                log.error("调度器异常:{}",e.getMessage());
            }
        }
    }

    @Override
    public void registerRobotWorker(Long robotId) {
        FREE_WORKER.add(robotId);
    }

    @Override
    public void removeTask(Task task) {

    }

    @Override
    public void registerRobot(Long robotId) {

    }

    @Override
    public void removeRobot(Long robotId) {
        FREE_WORKER.remove(robotId);
    }

    @Override
    public void initRobot() {
        FREE_WORKER.addAll(manager.getAllRobot().keySet());
        new Thread(this::mainThread).start();
    }

    private boolean canChangeTaskStatus(String taskId){
        return TASK_STATUS.remove(taskId)!=null&&manager.removeWorldTask(taskId)!=null;
    }

}
