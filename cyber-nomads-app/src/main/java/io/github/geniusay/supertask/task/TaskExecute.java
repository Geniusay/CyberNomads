package io.github.geniusay.supertask.task;

@FunctionalInterface
public interface TaskExecute {
    Object execute(Task task);
}
