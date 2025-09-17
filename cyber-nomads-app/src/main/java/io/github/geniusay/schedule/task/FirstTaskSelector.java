package io.github.geniusay.schedule.task;

import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.schedule.TaskScheduleManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
               log.info("找到合适的任务,robotName:{},taskName:{}",worker.getNickname(), task.getTaskName());
                return task;
            }
        }
        return null;
    }

    private Boolean taskCanDo(Task task,RobotWorker worker){
        return task.getTerminator().robotCanDo(worker) && Objects.equals(task.getUid(), worker.getUid());
    }

    private Boolean taskTypeIsShared(Task task,RobotWorker worker){
        return !Objects.equals(task.getUid(), worker.getUid());
    }
}
