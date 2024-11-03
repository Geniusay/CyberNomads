package io.github.geniusay.core.supertask.task;

import io.github.geniusay.core.exception.ServeException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 任务参数帮助列表
 */
public interface ParamsHelper {

    /**
     * 提供需要的参数列表，
     * @return
     */
    List<TaskNeedParams> supplierNeedParams();

    /**
     * 从前端返回的params中，取出需要的
     * @param params
     * @return
     */
    default Map<String, Object> getParams(Map<String, Object> params){
        Map<String, Object> pluginParams = new HashMap<>();
        for (TaskNeedParams needParam : supplierNeedParams()) {
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
