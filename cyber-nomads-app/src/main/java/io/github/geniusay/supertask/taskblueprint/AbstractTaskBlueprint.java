package io.github.geniusay.supertask.taskblueprint;

import io.github.geniusay.pojo.Platform;

public abstract class AbstractTaskBlueprint implements TaskBlueprint{

    /**
     * 平台类型
     * @return
     */
    public abstract Platform platform();

    /**
     * 任务类型
     * @return
     */
    public abstract String taskType();

}
