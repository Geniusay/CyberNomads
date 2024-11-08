package io.github.geniusay.core.supertask.taskblueprint;

import io.github.geniusay.core.supertask.task.LastWordHandler;
import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.core.supertask.task.TaskExecute;

public abstract class AbstractTaskBlueprint implements TaskBlueprint {

    public abstract String platform();
    public abstract String taskType();

    @Override
    public TaskExecute supplierExecute() {
        return (robot) -> {
            Task task = robot.task();
            try {
                executeTask(robot, task);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return null;
        };
    }

    @Override
    public LastWordHandler supplierLastWordHandler() {
        return (robot) -> {
           robot.task().getTerminator().doTask(robot);
           return lastWord(robot, robot.task());
        };
    }

    protected abstract void executeTask(RobotWorker robot, Task task) throws Exception;

    protected abstract String lastWord(RobotWorker robot, Task task);
}
