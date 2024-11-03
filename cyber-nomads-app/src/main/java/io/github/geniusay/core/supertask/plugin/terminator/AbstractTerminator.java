package io.github.geniusay.core.supertask.plugin.terminator;

import io.github.geniusay.pojo.DO.TaskDO;
import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.utils.ConvertorUtil;

import java.util.List;
import java.util.Map;

public abstract class AbstractTerminator implements Terminator {

    protected TaskDO taskDO;

    private Map<String, Object> params;

    public AbstractTerminator(TaskDO taskDO) {
        this.taskDO = taskDO;
        this.params = ConvertorUtil.jsonStringToMap(taskDO.getParams());
    }

    /**
     * 从 TaskDO 的 params 中获取参数
     */
    protected <T> T getParam(String key, Class<T> clazz) {
        return (T) params.get(key);
    }

    @Override
    public abstract boolean doTask(RobotWorker worker);

    @Override
    public abstract boolean taskIsDone();
}