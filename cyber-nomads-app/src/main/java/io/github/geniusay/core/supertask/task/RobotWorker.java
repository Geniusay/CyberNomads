package io.github.geniusay.core.supertask.task;

import io.github.geniusay.core.supertask.config.TaskStatus;
import io.github.geniusay.pojo.DO.RobotDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.stream.Stream;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RobotWorker {

    private Long id;
    private String nickname;
    private String username;
    private String cookie;

    private volatile Task currentTask;

    public RobotWorker(RobotDO robotDO) {
        this.id = robotDO.getId();
        this.nickname = robotDO.getNickname();
        this.username = robotDO.getUsername();
        this.cookie = robotDO.getCookie();
    }

    public Task task() {
        return currentTask;
    }

    public synchronized boolean execute(){
        if (currentTask!=null&&canExecuteTask()) {
            task().getExecute().execute(this);
            return true;
        }
        return false;
    }

    private boolean canExecuteTask(){
        return Stream.of(TaskStatus.RUNNING, TaskStatus.EXCEPTION).anyMatch(e->e.equals(currentTask.getTaskStatus()));
    }

    public synchronized void setTask(Task task) {
        this.currentTask = task;
    }

    public boolean isWorking(){
        return !Objects.isNull(currentTask);
    }
}
