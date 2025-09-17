package io.github.geniusay.schedule.task;

import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.pojo.DO.RobotDO;
import io.github.geniusay.schedule.TaskScheduleManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/13 17:22
 */
@Component
@Slf4j
public class FirstTaskSelector implements TaskSelector {
    @Resource
    TaskScheduleManager manager;
    @Override
    public Task select(RobotWorker worker) {
        Map<String, Task> taskMap = manager.getRobotTaskById(worker.getId());
        List<Task> tasks = new ArrayList<>(taskMap.values());
        for (Task task : tasks) {
           if(taskCanDo(task,worker)){
               log.info("【{}】找到合适的任务,taskName:{}",worker.getNickname(), task.getTaskName());
                return task;
            }
        }
        return null;
    }

    private Boolean taskCanDo(Task task,RobotWorker worker){
        List<RobotDO> robots = task.getRobots();
        Set<Long> set = robots.stream().map(RobotDO::getId).collect(Collectors.toSet());
        if (!set.contains(worker.getId())) {
            return false;
        }
        return task.getTerminator().robotCanDo(worker) && Objects.equals(task.getUid(), worker.getUid());
    }

    private Boolean taskTypeIsShared(Task task,RobotWorker worker){
        return !Objects.equals(task.getUid(), worker.getUid());
    }
}
