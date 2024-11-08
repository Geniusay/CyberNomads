package io.github.geniusay.core.supertask.plugin;


import io.github.geniusay.core.supertask.task.ParamsHelper;

public interface TaskPlugin extends ParamsHelper {


    /**
     * 一个功能插件组请重写该方法，如：一个功能插件的抽象类AbstractTerminator，
     * @return 返回功能插件组名称
     */
    String getPluginGroup();

}
