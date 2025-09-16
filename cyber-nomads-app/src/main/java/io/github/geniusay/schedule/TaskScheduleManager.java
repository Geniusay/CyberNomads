package io.github.geniusay.schedule;

import io.github.geniusay.core.exception.ServeException;
import io.github.geniusay.core.supertask.TaskFactory;
import io.github.geniusay.core.supertask.config.TaskStatus;
import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.pojo.DO.RobotDO;
import io.github.geniusay.pojo.DO.TaskDO;
import io.github.geniusay.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/1 20:14
 */
@Component
@Slf4j
public class TaskScheduleManager {
    @Resource
    TaskFactory taskFactory;
    @Resource
    TaskEventMan EVENT_PUBLISHER;
    @Resource
    TaskService taskService;
    @Resource
    WorkerExecute workerExecute;
    private static final Map<String, Task> WORLD_TASK = new ConcurrentHashMap<>();
    private static final Map<Long, RobotWorker> WORLD_ROBOTS = new ConcurrentHashMap<>();
    private static final Map<Long, Map<String,Task>> WORLD_ROBOTS_TASK = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Long, ReentrantLock> LOCK_MAP = new ConcurrentHashMap<>();
    @PostConstruct
    public void init() throws InterruptedException {
        List<TaskDO> taskDOS = taskService.getTaskByStatus(List.of(TaskStatus.RUNNING.name(),TaskStatus.EXCEPTION.name()));
        if(taskDOS!=null){
            List<TaskDO> readyTasks = taskService.populateRobotListForTasks(taskDOS);
            for (TaskDO taskDO : readyTasks) {
                Task task = taskFactory.buildTask(taskDO, taskDO.getPlatform(), taskDO.getTaskType());
                WORLD_TASK.put(String.valueOf(taskDO.getId()), task);
                List<RobotDO> robots = task.getRobots();
                robots.forEach(robotDO -> {
                    WORLD_ROBOTS.put(robotDO.getId(), new RobotWorker(robotDO));
                    Map<String, Task> taskMap = WORLD_ROBOTS_TASK.getOrDefault(robotDO.getId(), new ConcurrentHashMap<>());
                    taskMap.put(String.valueOf(taskDO.getId()), task);
                    WORLD_ROBOTS_TASK.put(robotDO.getId(), taskMap);
                    workerExecute.push(robotDO.getId());
                });
            }
        }
//        EVENT_PUBLISHER.initRobot();
    }

    public void registerTaskAndStart(TaskDO taskdo){
        TaskDO taskDO = taskService.populateRobotListForTasks(List.of(taskdo)).get(0);
        Task task = taskFactory.buildTask(taskDO, taskDO.getPlatform(), taskDO.getTaskType());
        WORLD_TASK.put(String.valueOf(taskDO.getId()), task);
        if(task.getRobots().isEmpty()){
            throw new ServeException("robot列表不能为空");
        }
        for (RobotDO robot : task.getRobots()) {
            ReentrantLock lock = LOCK_MAP.computeIfAbsent(robot.getId(), id -> new ReentrantLock());
            lock.lock();
            try {
                WORLD_ROBOTS_TASK.computeIfAbsent(robot.getId(), id -> new ConcurrentHashMap<>())
                        .put(String.valueOf(taskDO.getId()), task);
                WORLD_ROBOTS.computeIfAbsent(robot.getId(), id -> {
                    EVENT_PUBLISHER.startWork(id);
                    workerExecute.push(robot.getId());
                    return new RobotWorker(robot);
                });
            }finally {
                lock.unlock();
            }
        }
    }

    public void registerRobot(RobotDO robotDO){
        WORLD_ROBOTS.put(robotDO.getId(),new RobotWorker(robotDO));
        EVENT_PUBLISHER.registerRobot(robotDO.getId());
    }

    public Collection<Task> getWorkerAllTask(Long robotId){
        return WORLD_ROBOTS_TASK.get(robotId).values();
    }

    public void removeWorkerTask(String taskId){
        Task task = WORLD_TASK.remove(taskId);
        if(!Objects.isNull(task)){
            task.getRobots().forEach((robotDO)->{
                WORLD_ROBOTS_TASK.get(robotDO.getId()).remove(taskId);
                ReentrantLock lock = LOCK_MAP.get(robotDO.getId());
                if(lock!=null){
                    lock.lock();
                    try {
                        if(WORLD_ROBOTS_TASK.get(robotDO.getId()).isEmpty()){
                            WORLD_ROBOTS_TASK.remove(robotDO.getId());
                            WORLD_ROBOTS.remove(robotDO.getId());
                            LOCK_MAP.remove(robotDO.getId());
                        }
                    }finally {
                        lock.unlock();
                    }
                }
            });
        }
    }

    //取消共享
    public void unshared(Long robotId){
        RobotWorker worker = WORLD_ROBOTS.get(robotId);
        if(worker==null)
            return;
        worker.setHasShared(false);
    }


    public Map<Long,RobotWorker> getAllRobot(){
        return WORLD_ROBOTS;
    }
    public Map<Long, Map<String,Task>> getWorldRobotsTask(){
        return WORLD_ROBOTS_TASK;
    }
    public Map<String, Task> getWorldTask(){
        return WORLD_TASK;
    }
    public Task getTaskById(String taskId){
        return WORLD_TASK.get(taskId);
    }
    public RobotWorker getRobotById(Long robotId){
        return WORLD_ROBOTS.get(robotId);
    }
    public Map<String,Task> getRobotTaskById(Long robotId){
        return WORLD_ROBOTS_TASK.get(robotId);
    }
    public Task removeWorldTask(String taskId){
        return WORLD_TASK.remove(taskId);
    }
    public void removeWorldRobot(Long robotId){
        WORLD_ROBOTS.remove(robotId);
        WORLD_ROBOTS_TASK.remove(robotId);
    }
    public void removeWorldRobotTask(Long robotId,String taskId){
        WORLD_ROBOTS_TASK.get(robotId).remove(taskId);
    }
}
