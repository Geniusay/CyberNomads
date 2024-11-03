package io.github.geniusay.core.supertask.plugin;

import io.github.geniusay.core.exception.ServeException;
import io.github.geniusay.core.supertask.task.TaskNeedParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface TaskPlugin {

    List<TaskNeedParams> pluginParams();

    default Map<String, Object> getParams(Map<String, Object> params){
        Map<String, Object> pluginParams = new HashMap<>();
        for (TaskNeedParams needParam : pluginParams()) {
            String name = needParam.getName();
            Object value = params.getOrDefault(name, needParam.getDefaultValue());
            if (Objects.isNull(value)&&needParam.isRequired()) {
                throw new ServeException(400, String.format("%s参数错误", name));
            }else{
                pluginParams.put(name, value);
            }
        }
        return pluginParams;
    }


}
