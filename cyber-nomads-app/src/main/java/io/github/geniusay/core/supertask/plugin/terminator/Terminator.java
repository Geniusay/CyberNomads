package io.github.geniusay.core.supertask.plugin.terminator;

import io.github.geniusay.core.supertask.plugin.TaskPlugin;
import io.github.geniusay.core.supertask.task.RobotWorker;

import static io.github.geniusay.core.supertask.config.PluginConstant.TERMINATOR_GROUP_NAME;

public interface Terminator extends TaskPlugin {

    boolean robotCanDo(RobotWorker worker);

    void doTask(RobotWorker worker);

    boolean taskIsDone();

}
