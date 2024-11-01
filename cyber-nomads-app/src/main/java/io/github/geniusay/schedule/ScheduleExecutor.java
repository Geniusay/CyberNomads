package io.github.geniusay.schedule;

import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.Task;
import lombok.extern.slf4j.Slf4j;
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
public class ScheduleExecutor implements TaskListener{
    @Resource
    private Executor taskExecutor;
    @Resource
    private TaskScheduleManager manager;
    private final BlockingQueue<Long> FREE_WORKER = new LinkedBlockingQueue<>();
    @PostConstruct
    public void init(){
        FREE_WORKER.addAll(manager.getAllRobot().keySet());
        new Thread(this::mainThread).start();
    }

    public void mainThread(){
        while (!Thread.currentThread().isInterrupted()){
            try {
                Long robotId = FREE_WORKER.take();
                RobotWorker robotWorker = manager.getAllRobot().get(robotId);
                Map<Long, Map<String, Task>> worldRobotsTask = manager.getWorldRobotsTask();
                List<Task> tasks = new ArrayList<>(worldRobotsTask.get(robotId).values());
                if (!tasks.isEmpty()) {
                    Task selectedTask = tasks.get(new Random().nextInt(tasks.size()));
                    robotWorker.setTask(selectedTask);
                    taskExecutor.execute(() -> {
                        try {
                            robotWorker.execute();
                            robotWorker.lastWord();
                            FREE_WORKER.add(robotId);
                            worldRobotsTask.get(robotId).remove(selectedTask.getId());
                        } catch (Exception e) {
                            log.error("robot执行异常:{}",e.getMessage());
                        }
                    });
                }
            } catch (InterruptedException e) {
                log.error("调度器异常:{}",e.getMessage());
            }
        }
    }

    @Override
    public void startTask(Task task) {

    }

    @Override
    public void removeTask(Task task) {

    }

    @Override
    public void registerRobot(Long robotId) {
        FREE_WORKER.add(robotId);
    }

    @Override
    public void removeRobot(Long robotId) {
        FREE_WORKER.remove(robotId);
    }

}
