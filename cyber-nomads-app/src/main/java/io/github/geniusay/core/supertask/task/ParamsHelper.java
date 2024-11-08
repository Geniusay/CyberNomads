package io.github.geniusay.core.supertask.task;

import com.alibaba.fastjson.JSON;
import io.github.geniusay.core.exception.ServeException;
import io.github.geniusay.utils.ParamsUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 任务参数帮助列表
 */
public interface ParamsHelper {

    /**
     * 提供需要的参数列表，
     * @return
     */
    default List<TaskNeedParams> supplierNeedParams() {
        return List.of();
    }

    /**
     * 从前端返回的params中，取出需要的
     * @param paramMap 前端传回来的参数列表
     * @return
     */
    default Map<String, Object> getParams(Map<String, Object> paramMap){
        Map<String, Object> pluginParams = new HashMap<>();
        ParamsUtil.validateAndInitParams(pluginParams, supplierNeedParams());
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
        return ParamsUtil.getValue(readyMap, name, clazz);
    }


}
