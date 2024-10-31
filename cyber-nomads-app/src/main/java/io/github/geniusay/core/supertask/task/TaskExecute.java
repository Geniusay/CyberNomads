package io.github.geniusay.core.supertask.task;

@FunctionalInterface
public interface TaskExecute {
    Object execute(RobotWorker robot);
}
