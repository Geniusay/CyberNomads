package io.github.geniusay.core.supertask.task;

import com.alibaba.fastjson.JSON;
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
     * @param paramMap 前端传回来的参数列表
     * @return
     */
    default Map<String, Object> getParams(Map<String, Object> paramMap){
        Map<String, Object> pluginParams = new HashMap<>();
        for (TaskNeedParams needParam : supplierNeedParams()) {
            String name = needParam.getName();
            Object value = paramMap.getOrDefault(name, needParam.getDefaultValue());
            if (Objects.isNull(value)&&needParam.isRequired()) {
                throw new ServeException(400, String.format("%s参数错误", name));
            }else{
                pluginParams.put(name, value);
            }
        }
        return pluginParams;
    }

    /**
     * 从已经从前端参数列表中，提取出来的参数:参数值 map获取对应的值
     * @param readyMap 已经提取的参数列表
     * @param name 参数名
     * @param clazz 转换的类型
     * @return 返回转化后的值
     */
    default <T> T getValue(Map<String,Object> readyMap,String name, Class<T> clazz){
        String jsonStr = JSON.toJSONString(readyMap.get(name));
        return JSON.parseObject(jsonStr, clazz);
    }
}
