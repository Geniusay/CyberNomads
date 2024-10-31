package io.github.geniusay.core.supertask.task;

@FunctionalInterface
public interface LastWordHandler {
    String lastTalk(RobotWorker worker);
}
