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
import org.apache.tomcat.util.threads.TaskThreadFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/1 20:14
 */
@Component
public class TaskScheduleManager {

    @Resource
    TaskMapper taskMapper;
    @Resource
    RobotMapper robotMapper;
    @Resource
    TaskFactory taskFactory;
    private static Map<Long, Task> wordsTask;
    private static Map<Long, RobotWorker> wordsRobots;
    private static Map<Long, Map<String,Task>> wordsRobotsTask;
    private static final ThreadPoolExecutor SCHEDULER = new ThreadPoolExecutor(50,100,0,TimeUnit.SECONDS,new SynchronousQueue<>(), new TaskThreadFactory("robot-schedule",false,Thread.NORM_PRIORITY), new ThreadPoolExecutor.DiscardPolicy());

    //TODO final map
    @PostConstruct
    public void init(){
        List<TaskDO> taskDOS = taskMapper.selectList(new QueryWrapper<TaskDO>().eq("task_status", TaskStatus.PENDING.name()));
        wordsTask = taskDOS.stream().collect(Collectors.toConcurrentMap(
                TaskDO::getId,
                taskDO -> taskFactory.buildTask(taskDO,taskDO.getPlatform(),taskDO.getTaskType())
        ));
        List<RobotDO> robotDOS = robotMapper.selectList(new QueryWrapper<RobotDO>().eq("ban", false).eq("has_delete", false));
        wordsRobots = robotDOS.stream().map(RobotWorker::new).collect(Collectors.toConcurrentMap(
                RobotWorker::getId,
                robotWorker -> robotWorker
        ));
        wordsRobotsTask = new ConcurrentHashMap<>(1024);
    }

    public void registerTask(TaskDO task){
        wordsTask.put(task.getId(),taskFactory.buildTask(task,task.getPlatform(),task.getTaskType()));
    }

    public Task removeTask(Long taskId){
        return wordsTask.remove(taskId);
    }

    public void registerRobot(RobotDO robotDO){
        wordsRobots.put(robotDO.getId(),new RobotWorker(robotDO));
    }

    public RobotWorker removeRobot(Long robotId){
        return wordsRobots.remove(robotId);
    }

    public Collection<Task> getWorkerAllTask(Long robotId){
        return wordsRobotsTask.get(robotId).values();
    }

    private void start0(Task task){
        List<RobotDO> robots = task.getRobots();
        for (RobotDO robot : robots) {
            RobotWorker worker = wordsRobots.get(robot.getId());
            if(worker==null){
                throw new RuntimeException("不存在的赛博账号");
            }
            Map<String, Task> taskMap = wordsRobotsTask.getOrDefault(robot.getId(), new ConcurrentHashMap<>());
            synchronized (worker){
                if (worker.task() != null) {
                    taskMap.put(task.getId(),task);
                }else{
                    worker.setTask(task);
                }
            }
            SCHEDULER.execute(()->{
                worker.task().getExecute().execute(worker);
            });
        }
    }

    public void removeWorkerTask(Long robotId,String taskId){
        Map<String,Task> taskMap;
        if ((taskMap = wordsRobotsTask.get(robotId)) != null) {
            Object o = taskMap.size() == 1 ? wordsRobotsTask.remove(robotId) : taskMap.remove(taskId);
        }else{
            wordsRobotsTask.remove(robotId);
        }
    }

}
