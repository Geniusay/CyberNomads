package io.github.geniusay.utils;

import com.alibaba.fastjson.JSON;
import io.github.geniusay.core.exception.ServeException;
import io.github.geniusay.core.supertask.task.TaskNeedParams;

import java.util.*;
import java.util.stream.Collectors;


public class ParamsUtil {
    @SafeVarargs
    public static List<TaskNeedParams> packageListParams(List<TaskNeedParams> ...taskNeedParams){
        ArrayList<TaskNeedParams> params = new ArrayList<>();
        for (List<TaskNeedParams> taskNeedParam : taskNeedParams) {
            params.addAll(taskNeedParam);
        }
        return params;
    }

    /**
     * 校验并提取参数列表
     * @param resMap 准备返回的参数列表
     * @param paramMap 原视参数列表
     * @param params 所需参数（蓝图提供）
     */
    public static void validateAndGetParams(Map<String, Object> resMap, Map<String, Object> paramMap, List<TaskNeedParams> params){
        for (TaskNeedParams needParam : params) {
            String name = needParam.getName();
            Object value = paramMap.getOrDefault(name, needParam.getDefaultValue());

            if(!TaskNeedParams.class.equals(needParam.getType())&&(needParam.isRequired()||value!=null)){
                validateParam(name, value, needParam);
                try {
                    resMap.put(name, Optional.ofNullable(value).map((var->{
                        String jsonStr = JSON.toJSONString(value);
                        return JSON.parseObject(jsonStr, needParam.getType());
                    })).orElse(null));
                }catch (Exception e){
                    throw new ServeException(400, String.format("%s参数类型错误", name));
                }
            }

            if(!needParam.getParams().isEmpty()){
                validateAndGetParams(resMap, paramMap, needParam.getParams());
            }else{
                validateAndGetParams(resMap, paramMap, needParam.getSelection());
            }
        }
    }

    public static void validateParam(String name, Object value, TaskNeedParams param){
        if (Objects.isNull(value)&&param.isRequired()) {
            throw new ServeException(400, String.format("%s参数错误", name));
        }
        if (!param.getSelection().isEmpty()) {
            Set<String> set = param.getSelection().stream().map(TaskNeedParams::getName).collect(Collectors.toSet());
            if (!set.contains((String)value)) {
                throw new ServeException(400, String.format("%s参数[%s]不匹配", name, value));
            }
        }
    }

    public static <T> T getValue(Map<String,Object> readyMap,String name, Class<T> clazz){
        String jsonStr = JSON.toJSONString(readyMap.get(name));
        return JSON.parseObject(jsonStr, clazz);
    }
}
