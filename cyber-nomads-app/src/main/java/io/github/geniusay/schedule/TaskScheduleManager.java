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
    TaskMapper taskMapper;
    @Resource
    RobotMapper robotMapper;
    @Resource
    TaskFactory taskFactory;
    @Resource
    TaskEventMan EVENT_PUBLISHER;
    @Resource
    TaskService taskService;
    @Resource
    RobotService robotService;
    public static final Map<String, Task> WORLD_TASK = new ConcurrentHashMap<>();
    public static final Map<Long, RobotWorker> WORLD_ROBOTS = new ConcurrentHashMap<>();
    public static final Map<Long, Map<String,Task>> WORLD_ROBOTS_TASK = new ConcurrentHashMap<>();


    //TODO final map
    @PostConstruct
    public void init(){
        List<TaskDO> taskDOS = taskMapper.selectList(new QueryWrapper<TaskDO>().eq("task_status", TaskStatus.PENDING.name()));
        if(taskDOS!=null){
            List<TaskDO> readyTasks = taskService.populateRobotListForTasks(taskDOS);
            for (TaskDO taskDO : readyTasks) {
                WORLD_TASK.put(taskDO.getUid(),taskFactory.buildTask(taskDO,taskDO.getPlatform(),taskDO.getTaskType()));
            }
        }
        List<RobotDO> robotDOS = robotService.queryVaildRobot();
        for (RobotDO robotDO : robotDOS) {
            WORLD_ROBOTS.put(robotDO.getId(),new RobotWorker(robotDO));
        }
        WORLD_TASK.forEach((k,v)->{
            v.setRobots(new ArrayList<>(robotDOS));
            List<RobotDO> robots = v.getRobots();
            if(robots!=null)
                for (RobotDO robot : robots) {
                    Map<String, Task> taskMap = WORLD_ROBOTS_TASK.getOrDefault(robot.getId(), new ConcurrentHashMap<>());
                    taskMap.put(k,v);
                    WORLD_ROBOTS_TASK.put(robot.getId(),taskMap);
                }
        });
        EVENT_PUBLISHER.initRobot();
    }

    public void registerTask(TaskDO task){
        WORLD_TASK.put(String.valueOf(task.getId()),taskFactory.buildTask(task,task.getPlatform(),task.getTaskType()));
        log.info("任务注册:{}",task.getId());
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

    public void startTask(String taskId){
        for (RobotDO robot : WORLD_TASK.get(taskId).getRobots()) {
            Map<String, Task> taskMap = WORLD_ROBOTS_TASK.getOrDefault(robot.getId(), new ConcurrentHashMap<>());
            taskMap.put(taskId,WORLD_TASK.get(taskId));
            WORLD_ROBOTS_TASK.put(robot.getId(),taskMap);
        }
        EVENT_PUBLISHER.startWork(WORLD_TASK.get(taskId));
    }

    public void removeWorkerTask(String taskId){
        Task task = WORLD_TASK.remove(taskId);
        WORLD_ROBOTS_TASK.forEach((k,v)->{
            v.remove(taskId);
        });
        EVENT_PUBLISHER.removeTask(task);
    }

    public Map<Long,RobotWorker> getAllRobot(){
        return WORLD_ROBOTS;
    }
    public Map<Long, Map<String,Task>> getWorldRobotsTask(){
        log.info(WORLD_ROBOTS_TASK.toString());
        return WORLD_ROBOTS_TASK;
    }
    public Map<String, Task> getWorldTask(){
        return WORLD_TASK;
    }
}
