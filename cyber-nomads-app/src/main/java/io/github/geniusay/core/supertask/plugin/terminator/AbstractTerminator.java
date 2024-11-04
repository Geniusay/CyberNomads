package io.github.geniusay.core.supertask.plugin.terminator;

import io.github.geniusay.pojo.DO.RobotDO;
import io.github.geniusay.pojo.DO.TaskDO;
import io.github.geniusay.core.supertask.task.RobotWorker;

import java.util.List;
import java.util.Map;

public abstract class AbstractTerminator implements Terminator {

    protected TaskDO taskDO;
    protected final Map<String, Object> params;
    protected List<RobotDO> robotList;

    public AbstractTerminator(TaskDO taskDO, Map<String, Object> params) {
        this.taskDO = taskDO;
        this.params = params;
        this.robotList = taskDO.getRobotList();
    }

    /**
     * 从 params 中获取参数
     */
    protected <T> T getParam(String key, Class<T> clazz) {
        return (T) params.get(key);
    }

    @Override
    public abstract boolean doTask(RobotWorker worker);

    @Override
    public abstract boolean taskIsDone();
}