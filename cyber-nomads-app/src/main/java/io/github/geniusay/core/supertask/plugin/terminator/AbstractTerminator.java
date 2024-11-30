package io.github.geniusay.core.supertask.plugin.terminator;

import io.github.geniusay.core.supertask.plugin.BaseTaskPlugin;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.core.supertask.task.RobotWorker;


import static io.github.geniusay.core.supertask.config.PluginConstant.TERMINATOR_GROUP_NAME;


public abstract class AbstractTerminator extends BaseTaskPlugin implements Terminator {

    boolean isDown = false;

    @Override
    public void init(Task task) {
        super.init(task);
    }

    @Override
    public abstract boolean robotCanDo(RobotWorker worker);

    @Override
    public abstract void doTask(RobotWorker worker);

    @Override
    public abstract boolean taskIsDone();

    @Override
    public void downTask() {
        isDown = true;
    }

    @Override
    public String getPluginGroup() {
        return TERMINATOR_GROUP_NAME;
    }
}
