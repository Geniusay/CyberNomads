package io.github.geniusay.core.supertask.plugin.terminator;

import io.github.geniusay.core.supertask.plugin.BaseTaskPlugin;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.pojo.DO.RobotDO;
import io.github.geniusay.pojo.DO.TaskDO;
import io.github.geniusay.core.supertask.task.RobotWorker;

import java.util.List;
import java.util.Map;

import static io.github.geniusay.core.supertask.config.PluginConstant.TERMINATOR_GROUP_NAME;


public abstract class AbstractTerminator extends BaseTaskPlugin implements Terminator {

    @Override
    public void init(Task task) {
        super.init(task);

    }


    @Override
    public abstract boolean doTask(RobotWorker worker);

    @Override
    public abstract boolean taskIsDone();

    @Override
    public String getPluginGroup() {
        return TERMINATOR_GROUP_NAME;
    }
}
