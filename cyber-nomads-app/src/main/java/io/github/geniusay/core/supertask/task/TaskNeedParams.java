package io.github.geniusay.core.supertask.task;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 提供给前端要输入的字段
 */
@Data
public class TaskNeedParams {

    private String name;

    // 前端输入框类型
    private String inputType;
    private Class<?> type;
    private String desc;
    private String extendDesc;
    private boolean required;
    private Object defaultValue;
    private boolean hidden = false;
    private List<TaskNeedParams> params = new ArrayList<>();
    private List<TaskNeedParams> selection = new ArrayList<>();

    // 构造方法（不带默认值、可选项、子参数）
    public TaskNeedParams(String name, Class<?> type, String desc) {
        this.name = name;
        this.type = type;
        this.desc = desc;
        this.extendDesc = null;
        this.required = true;
        this.inputType = type == Boolean.class ? InputTypeEnum.BOOLEAN.getValue() : InputTypeEnum.INPUT.getValue();
        this.defaultValue = null;
    }

    // 构造方法（带默认值，不带可选项、子参数）
    public TaskNeedParams(String name, Class<?> type, String desc, boolean required, Object defaultValue) {
        this.name = name;
        this.type = type;
        this.desc = desc;
        this.required = required;
        this.inputType = type == Boolean.class ? InputTypeEnum.BOOLEAN.getValue() : InputTypeEnum.INPUT.getValue();
        this.defaultValue = defaultValue;
    }

    // 构造方法（带可选项和子参数）
    public TaskNeedParams(String name, Class<?> type, String desc, boolean required, Object defaultValue, List<TaskNeedParams> selection, List<TaskNeedParams> params, String extendDesc, boolean hidden) {
        this.name = name;
        this.type = type;
        this.desc = desc;
        this.required = required;
        this.defaultValue = defaultValue;
        this.inputType = type == Boolean.class ? InputTypeEnum.BOOLEAN.getValue() : InputTypeEnum.INPUT.getValue();
        this.selection = selection == null ? new ArrayList<>() : selection;
        this.params = params == null ? new ArrayList<>() : params;
        this.extendDesc = extendDesc;
        this.hidden = hidden;
    }

    // 构造方法（不带类型，用于表示复杂参数结构）
    public TaskNeedParams(String name, String desc, boolean required, List<TaskNeedParams> selection, List<TaskNeedParams> params) {
        this.name = name;
        this.type = null;
        this.desc = desc;
        this.required = required;
        this.inputType = InputTypeEnum.INPUT.getValue();
        this.selection = selection == null ? new ArrayList<>() : selection;
        this.params = params == null ? new ArrayList<>() : params;
    }

    // 构造方法（带可选项和子参数）
    public TaskNeedParams(String name, Class<?> type, String desc, boolean required, Object defaultValue, List<TaskNeedParams> selection, List<TaskNeedParams> params, InputTypeEnum inputType) {
        this.name = name;
        this.type = type;
        this.desc = desc;
        this.required = required;
        this.inputType = inputType.getValue();
        this.defaultValue = defaultValue;
        this.selection = selection == null ? new ArrayList<>() : selection;
        this.params = params == null ? new ArrayList<>() : params;
    }

    /**
     * 必填项
     */
    public static TaskNeedParams ofKV(String name, Object defaultValue, String desc) {
        return new TaskNeedParams(name, defaultValue.getClass(), desc, true, defaultValue, null, null, "", false);
    }

    public static TaskNeedParams ofKV(String name, Object defaultValue, String desc, String extendDesc) {
        return new TaskNeedParams(name, defaultValue.getClass(), desc, true, defaultValue, null, null, extendDesc, false);
    }

    public static TaskNeedParams ofKV(String name, Object defaultValue, String desc, String extendDesc, boolean hidden) {
        return new TaskNeedParams(name, defaultValue.getClass(), desc, true, defaultValue, null, null, extendDesc, hidden);
    }


    /**
     * 非必填项
     */
    public static TaskNeedParams ofK(String name, Class<?> type, String desc) {
        return new TaskNeedParams(name, type, desc, false, null, null, null, "", false);
    }

    public static TaskNeedParams ofK(String name, Class<?> type, String desc, String extendDesc) {
        return new TaskNeedParams(name, type, desc, false, null, null, null, extendDesc, false);
    }

    public static TaskNeedParams ofK(String name, Class<?> type, String desc, String extendDesc, boolean hidden) {
        return new TaskNeedParams(name, type, desc, false, null, null, null, extendDesc, hidden);
    }

    public static TaskNeedParams ofSelection(String name, String defaultValue, String desc, List<TaskNeedParams> selections) {
        return new TaskNeedParams(name, String.class, desc, true, defaultValue, selections, null, InputTypeEnum.SELECT);
    }

    public static TaskNeedParams ofParams(String name, String desc, List<TaskNeedParams> params) {
        return new TaskNeedParams(name, TaskNeedParams.class, desc, false, null, null, params, "", false);
    }

    public TaskNeedParams setInputType(InputTypeEnum inputType) {
        this.inputType = inputType.getValue();
        return this;
    }

    public enum InputTypeEnum {
        SELECT("selection"),
        INPUT("input"),
        TEXTAREA("textarea"),
        BOOLEAN("Boolean");

        private String value;

        InputTypeEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}