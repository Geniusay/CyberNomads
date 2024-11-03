package io.github.geniusay.core.supertask.plugin.terminator;

import io.github.geniusay.core.supertask.plugin.TaskPlugin;
import io.github.geniusay.core.supertask.task.RobotWorker;

public interface Terminator extends TaskPlugin {

    boolean doTask(RobotWorker worker);

    boolean taskIsDone();
}
