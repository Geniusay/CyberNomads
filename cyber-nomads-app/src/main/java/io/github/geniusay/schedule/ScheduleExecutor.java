package io.github.geniusay.schedule;

import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.Task;
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
    private final BlockingQueue<Long> FREE_WORKER = new LinkedBlockingQueue<>();

    public void mainThread(){
        while (true){
            try {
                Long robotId = FREE_WORKER.take();
                Map<Long, Map<String, Task>> worldRobotsTask = manager.getWorldRobotsTask();
                log.info("获取到空闲罗伯特ID:{}",robotId);
                Map<String, Task> taskMap = worldRobotsTask.get(robotId);

                if(Objects.nonNull(taskMap)){
                    List<Task> tasks = new ArrayList<>(taskMap.values());
                    log.info("任务集:{}",tasks);
                    if (!tasks.isEmpty()) {
                        RobotWorker robotWorker = manager.getAllRobot().get(robotId);
                        Task selectedTask = tasks.get(new Random().nextInt(tasks.size()));
                        robotWorker.setTask(selectedTask);
                        taskExecutor.execute(() -> {
                            try {
                                robotWorker.execute();
                                robotWorker.lastWord();
//                            worldRobotsTask.get(robotId).remove(selectedTask.getId());
                                System.out.println("robot任务执行完毕");
                                taskMap.remove(robotWorker.task().getUid());
                            } catch (Exception e) {
                                log.error("robot执行异常:{}", e.getMessage());
                            }
                        });
                    }
                }else{
                    Thread.sleep(1000);
                }
                FREE_WORKER.add(robotId);
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

    @Override
    public void initRobot() {
        FREE_WORKER.addAll(manager.getAllRobot().keySet());
        log.info("当前罗伯特数量:{}",FREE_WORKER.size());
        new Thread(this::mainThread).start();
    }

}
