package io.github.geniusay.core.supertask.terminator;

import io.github.geniusay.core.supertask.task.RobotWorker;

public interface Terminator {

    boolean doTask(RobotWorker worker);

    boolean taskIsDone();
}
