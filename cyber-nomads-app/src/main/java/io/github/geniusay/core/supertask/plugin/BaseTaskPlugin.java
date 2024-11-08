package io.github.geniusay.core.supertask.plugin;

import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import io.github.geniusay.pojo.DO.TaskDO;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BaseTaskPlugin implements TaskPlugin{


    protected Task task;

    protected Map<String, Object> pluginParams;


    public void init(Task task){
        this.task = task;
        this.pluginParams = getParams(task.getParams());
    }

    /**
     * 参数化轻量级初始化
     */
    protected BaseTaskPlugin(){

    }

    public static List<TaskNeedParams> getSupplierPluginParams(){
        return new BaseTaskPlugin().supplierNeedParams();
    }


    @Override
    public String getPluginGroup() {
        return "";
    }

    /**
     * 从 params 中获取参数
     */
    protected <T> T getParam(String key, Class<T> clazz) {
        return clazz.cast(pluginParams.get(key));
    }

    public static String getPluginName(Class<? extends BaseTaskPlugin> clazz){
        String defaultName = clazz.getName();
        if (clazz.isAnnotationPresent(Component.class)) {
            // 获取 @Component 注解实例
            Component component = clazz.getAnnotation(Component.class);
            // 返回注解的 value 值
            return Optional.of(component.value()).map(name -> StringUtils.isBlank(name) ? defaultName : name).get();
        }
        return defaultName;
    }
}
