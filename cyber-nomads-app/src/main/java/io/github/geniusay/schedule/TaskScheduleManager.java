package io.github.geniusay.schedule;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.geniusay.core.supertask.TaskFactory;
import io.github.geniusay.core.supertask.config.TaskStatus;
import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.mapper.RobotMapper;
import io.github.geniusay.mapper.TaskMapper;
import io.github.geniusay.pojo.DO.RobotDO;
import io.github.geniusay.pojo.DO.TaskDO;
import io.github.geniusay.service.RobotService;
import io.github.geniusay.service.TaskService;
import io.github.geniusay.utils.ConvertorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/1 20:14
 */
@Component
@Slf4j
@DependsOn("ITaskService")
public class TaskScheduleManager {
    @Resource
    TaskFactory taskFactory;
    @Resource
    TaskEventMan EVENT_PUBLISHER;
    @Resource
    TaskService taskService;
    public static final Map<String, Task> WORLD_TASK = new ConcurrentHashMap<>();
    public static final Map<Long, RobotWorker> WORLD_ROBOTS = new ConcurrentHashMap<>();
    public static final Map<Long, Map<String,Task>> WORLD_ROBOTS_TASK = new ConcurrentHashMap<>();


    //TODO final map
    @PostConstruct
    public void init(){
        List<TaskDO> taskDOS = taskService.getTaskByStatus(List.of(TaskStatus.RUNNING.name(),TaskStatus.EXCEPTION.name()));
        if(taskDOS!=null){
            List<TaskDO> readyTasks = taskService.populateRobotListForTasks(taskDOS);
            for (TaskDO taskDO : readyTasks) {
                Task task = taskFactory.buildTask(taskDO, taskDO.getPlatform(), taskDO.getTaskType());
                WORLD_TASK.put(String.valueOf(taskDO.getId()), task);
                List<RobotDO> robots = task.getRobots();
                robots.stream().forEach((robotDO)->WORLD_ROBOTS.put(robotDO.getId(),new RobotWorker(robotDO)));
                robots.stream().forEach(robot -> {
                            Map<String, Task> taskMap = WORLD_ROBOTS_TASK.getOrDefault(robot.getId(), new ConcurrentHashMap<>());
                            taskMap.put(String.valueOf(taskDO.getId()), task);
                            WORLD_ROBOTS_TASK.put(robot.getId(), taskMap);
                        });
            }
        }
        EVENT_PUBLISHER.initRobot();
    }

    public void registerTaskAndStart(TaskDO taskdo){
        TaskDO taskDO = taskService.populateRobotListForTasks(List.of(taskdo)).get(0);

        Task task = taskFactory.buildTask(taskDO, taskDO.getPlatform(), taskDO.getTaskType());
        WORLD_TASK.put(String.valueOf(taskDO.getId()), task);

        for (RobotDO robot : taskDO.getRobotList()) {
            WORLD_ROBOTS.put(robot.getId(),new RobotWorker(robot));
            Map<String, Task> taskMap = WORLD_ROBOTS_TASK.getOrDefault(robot.getId(), new ConcurrentHashMap<>());
            taskMap.put(String.valueOf(taskDO.getId()),WORLD_TASK.get(String.valueOf(taskDO.getId())));
            WORLD_ROBOTS_TASK.put(robot.getId(),taskMap);
        }
        EVENT_PUBLISHER.startWork(WORLD_TASK.get(String.valueOf(taskDO.getId())));
    }

    public Task removeTask(Long taskId){
        return WORLD_TASK.remove(taskId);
    }

    public void registerRobot(RobotDO robotDO){
        WORLD_ROBOTS.put(robotDO.getId(),new RobotWorker(robotDO));
        EVENT_PUBLISHER.registerRobot(robotDO.getId());
    }

    public RobotWorker removeRobot(Long robotId){
        EVENT_PUBLISHER.removeRobot(robotId);
        return WORLD_ROBOTS.remove(robotId);
    }

    public Collection<Task> getWorkerAllTask(Long robotId){
        return WORLD_ROBOTS_TASK.get(robotId).values();
    }

    public void removeWorkerTask(String taskId){
        Task task = WORLD_TASK.remove(taskId);
        task.getRobots().forEach((taskDO)->{
            WORLD_ROBOTS_TASK.get(taskDO.getId()).remove(taskId);
        });
        EVENT_PUBLISHER.removeTask(task);
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
}
