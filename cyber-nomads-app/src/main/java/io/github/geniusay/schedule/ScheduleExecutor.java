package io.github.geniusay.schedule;

import io.github.geniusay.constants.TaskActionConstant;
import io.github.geniusay.core.supertask.config.TaskStatus;
import io.github.geniusay.core.supertask.plugin.terminator.Terminator;
import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.pojo.DO.RobotDO;
import io.github.geniusay.service.Impl.TaskStatusManager;
import io.github.geniusay.service.TaskService;
import io.github.geniusay.utils.LastWordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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
    private final BlockingQueue<Long> FREE_WORKER = new LinkedBlockingQueue<>();
    private final ConcurrentHashMap<String,String> TASK_STATUS = new ConcurrentHashMap<>();

    public void mainThread(){
        while (true){
            try {
                Long robotId = FREE_WORKER.take();
                Map<Long, Map<String, Task>> worldRobotsTask = manager.getWorldRobotsTask();
                Map<String, Task> taskMap = worldRobotsTask.get(robotId);
                if(Objects.nonNull(taskMap)){
                    List<Task> tasks = new ArrayList<>(taskMap.values());
                    if (!tasks.isEmpty()) {
                        RobotWorker robotWorker = manager.getAllRobot().get(robotId);
                        Task selectedTask = tasks.get(new Random().nextInt(tasks.size()));
                        while (!selectedTask.getTerminator().doTask(robotWorker) && tasks.size()>1) {
                            tasks.remove(selectedTask);
                            selectedTask = tasks.get(new Random().nextInt(tasks.size()));
                        }
                        robotWorker.setTask(selectedTask);
                        TASK_STATUS.put(selectedTask.getId(),TaskActionConstant.START);
                        taskExecutor.execute(() -> {
                            try {
                                robotWorker.execute();
                            } catch (Exception e) {
                                log.error("robot执行异常:{},robot信息:{}", e.getMessage()+":"+e.getStackTrace()[0],robotWorker.getId());
                                taskStatusManager.modifyTask(Long.valueOf(robotWorker.task().getId()),TaskActionConstant.EXCEPTION);
                                TASK_STATUS.put(robotWorker.task().getId(),TaskActionConstant.EXCEPTION);
                            }finally {
                                boolean success = LastWordUtil.isSuccess(robotWorker.task().getLastWord().lastTalk(robotWorker));
                                taskMap.remove(robotWorker.task().getUid());
                                String taskId = robotWorker.task().getId();
                                if(robotWorker.task().getTerminator().taskIsDone() && needChangeTaskStatus(taskId)){
                                    taskStatusManager.modifyTask(Long.valueOf(taskId), TaskActionConstant.FINISH);
                                    manager.getWorldTask().remove(taskId);
                                    TASK_STATUS.remove(taskId);
                                }
                            }
                        });
                    }
                }
            } catch (InterruptedException e) {
                log.error("调度器异常:{}",e.getMessage());
            }
        }
    }

    @Override
    public void startTask(Task task) {
        for (RobotDO robot : task.getRobots()) {
            FREE_WORKER.add(robot.getId());
        }
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

    private boolean needChangeTaskStatus(String taskId){
        return TASK_STATUS.remove(taskId)!=null;
    }

}
