package io.github.geniusay.supertask.task;

@FunctionalInterface
public interface LogHandler {
    void log(Task task);
}
