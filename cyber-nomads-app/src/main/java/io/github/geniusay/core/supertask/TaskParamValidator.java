package io.github.geniusay.core.supertask;

import io.github.geniusay.core.supertask.task.TaskNeedParams;

import java.util.List;
import java.util.Map;

public class TaskParamValidator {

    /**
     * 验证前端传递的参数是否符合 TaskNeedParams 的要求
     */
    public static void validateParams(List<TaskNeedParams> needParams, Map<String, Object> providedParams) throws IllegalArgumentException {
        for (TaskNeedParams param : needParams) {
            String paramName = param.getName();
            Object value = providedParams.get(paramName);

            // 检查必填参数是否存在
            if (param.isRequired() && value == null) {
                throw new IllegalArgumentException("缺少必填参数: " + paramName);
            }

            // 如果参数存在，检查类型是否匹配
            if (value != null && !isTypeMatch(value, param.getType())) {
                throw new IllegalArgumentException("参数类型不匹配: " + paramName + " 需要类型: " + param.getType());
            }
        }
    }

    /**
     * 检查参数类型是否匹配
     */
    private static boolean isTypeMatch(Object value, String expectedType) {
        switch (expectedType.toLowerCase()) {
            case "string":
                return value instanceof String;
            case "integer":
                return value instanceof Integer;
            case "boolean":
                return value instanceof Boolean;
            default:
                return false;
        }
    }
}