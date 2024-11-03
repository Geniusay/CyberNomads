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
    public static final Map<String, Task> WORLD_TASK = new ConcurrentHashMap<>();
    public static final Map<Long, RobotWorker> WORLD_ROBOTS = new ConcurrentHashMap<>();
    public static final Map<Long, Map<String,Task>> WORLD_ROBOTS_TASK = new ConcurrentHashMap<>();


    //TODO final map
    @PostConstruct
    public void init(){
        List<TaskDO> taskDOS = taskMapper.selectList(new QueryWrapper<TaskDO>().eq("task_status", TaskStatus.PENDING.name()));
        if(taskDOS!=null){
            List<TaskDO> taskDOSFill = populateRobotListForTasks(taskDOS);
            for (TaskDO taskDO : taskDOSFill) {
                WORLD_TASK.put(taskDO.getUid(),taskFactory.buildTask(taskDO,taskDO.getPlatform(),taskDO.getTaskType()));
            }
        }
        List<RobotDO> robotDOS = robotMapper.selectList(new QueryWrapper<RobotDO>().eq("ban", 0).eq("has_delete", 0));
        for (RobotDO robotDO : robotDOS) {
            WORLD_ROBOTS.put(robotDO.getId(),new RobotWorker(robotDO));
        }
        WORLD_TASK.forEach((k,v)->{
            v.setRobots(new ArrayList<>(robotDOS));
            List<RobotDO> robots = v.getRobots();
            if(robots!=null)
                for (RobotDO robot : robots) {
                    log.info("添加罗伯特:{},任务:{}",robot.getId(),k);
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

    public List<TaskDO> populateRobotListForTasks(List<TaskDO> taskDOList) {
        // 1. 创建一个 Set 来收集所有任务中的机器人 ID，避免重复查询
        Set<Long> allRobotIds = new HashSet<>();

        // 2. 遍历任务列表，解析每个任务中的 robots 字符串，收集机器人 ID
        for (TaskDO task : taskDOList) {
            List<Long> robotIds = ConvertorUtil.stringToList(task.getRobots());
            if (robotIds != null && !robotIds.isEmpty()) {
                allRobotIds.addAll(robotIds);
            }
        }

        // 3. 如果没有任何机器人 ID，直接返回原任务列表
        if (allRobotIds.isEmpty()) {
            return taskDOList;
        }

        // 4. 使用 robotMapper 批量查询所有的机器人信息
        List<RobotDO> allRobots = robotMapper.selectBatchIds(allRobotIds);

        // 5. 将查询到的机器人信息转换为 Map，方便后续根据 ID 进行查找
        Map<Long, RobotDO> robotMap = allRobots.stream()
                .collect(Collectors.toMap(RobotDO::getId, robot -> robot));

        // 6. 遍历任务列表，填充每个任务的 robotList 字段
        for (TaskDO task : taskDOList) {
            List<Long> robotIds = ConvertorUtil.stringToList(task.getRobots());
            if (robotIds != null && !robotIds.isEmpty()) {
                // 根据 robotIds 从 robotMap 中获取对应的机器人信息
                List<RobotDO> robotList = robotIds.stream()
                        .map(robotMap::get)
                        .filter(Objects::nonNull)  // 过滤掉可能为 null 的机器人
                        .collect(Collectors.toList());

                // 填充到任务的 robotList 字段
                task.setRobotList(robotList);
            }
        }

        // 7. 返回填充了 robotList 的任务列表
        return taskDOList;
    }
}
