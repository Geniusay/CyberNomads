package io.github.geniusay.schedule;

import io.github.geniusay.core.supertask.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/1 23:33
 */
@Component
public class TaskEventMan {
    private List<TaskListener> listeners = new ArrayList<>();
    @Resource
    ApplicationContext applicationContext;
    @PostConstruct
    public void registerListener() {
        Map<String, TaskListener> beansOfType = applicationContext.getBeansOfType(TaskListener.class);
        beansOfType.forEach((k,v)->{
            listeners.add(v);
        });
    }

    public void startWork(Task task) {
        for (TaskListener listener : listeners) {
            listener.startTask(task);
        }
    }

    public void removeTask(Task task) {
        for (TaskListener listener : listeners) {
            listener.removeTask(task);
        }
    }

    public void registerRobot(Long robotId){
        for (TaskListener listener : listeners) {
            listener.registerRobot(robotId);
        }
    }

    public void removeRobot(Long robotId){
        for (TaskListener listener : listeners) {
            listener.removeRobot(robotId);
        }
    }

    public void initRobot(){
        for (TaskListener listener : listeners) {
            listener.initRobot();
        }
    }
}
