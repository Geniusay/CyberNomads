package io.github.geniusay.core.supertask.plugin.terminator;

import io.github.geniusay.core.supertask.plugin.BaseTaskPlugin;
import io.github.geniusay.pojo.DO.RobotDO;
import io.github.geniusay.pojo.DO.TaskDO;
import io.github.geniusay.core.supertask.task.RobotWorker;

import java.util.List;
import java.util.Map;


public abstract class AbstractTerminator extends BaseTaskPlugin implements Terminator {

    protected List<RobotDO> robotList;

    @Override
    public void init(TaskDO taskDO, Map<String, Object> params) {
        super.init(taskDO, params);
        this.robotList = taskDO.getRobotList();
    }


    @Override
    public abstract boolean doTask(RobotWorker worker);

    @Override
    public abstract boolean taskIsDone();
}
