package io.github.geniusay.core.supertask.task;


import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 提供给前端要输入的字段
 */
@Data
@Builder
public class TaskNeedParams {

    private String name;
    private Class<?> type;
    private String desc;
    private boolean required;
    private Object defaultValue;
    private List<TaskNeedParams> params = new ArrayList<>();
    private List<TaskNeedParams> selection = new ArrayList<>();


    // 构造方法（不带默认值、可选项、子参数）
    public TaskNeedParams(String name, Class<?> type, String desc) {
        this.name = name;
        this.type = type;
        this.desc = desc;
        this.required = true;
        this.defaultValue = null;
    }

    // 构造方法（带默认值，不带可选项、子参数）
    public TaskNeedParams(String name, Class<?> type, String desc, boolean required, Object defaultValue) {
        this.name = name;
        this.type = type;
        this.desc = desc;
        this.required = required;
        this.defaultValue = defaultValue;
    }

    // 构造方法（带可选项和子参数）
    public TaskNeedParams(String name, Class<?> type, String desc, boolean required, Object defaultValue, List<TaskNeedParams> selection, List<TaskNeedParams> params) {
        this.name = name;
        this.type = type;
        this.desc = desc;
        this.required = required;
        this.defaultValue = defaultValue;
        this.selection = selection == null ? new ArrayList<>() : selection;
        this.params = params == null ? new ArrayList<>() : params;
    }

    // 构造方法（不带类型，用于表示复杂参数结构）
    public TaskNeedParams(String name, String desc, boolean required, List<TaskNeedParams> selection, List<TaskNeedParams> params) {
        this.name = name;
        this.type = null;
        this.desc = desc;
        this.required = required;
        this.selection = selection == null ? new ArrayList<>() : selection;
        this.params = params == null ? new ArrayList<>() : params;
    }

    /**
     * 必填项
     * @param name
     * @param defaultValue
     * @param desc
     * @return
     */
    public static TaskNeedParams ofKV(String name, Object defaultValue, String desc){
        return TaskNeedParams.builder()
                .name(name)
                .defaultValue(defaultValue)
                .required(true)
                .desc(desc)
                .type(defaultValue.getClass())
                .build();
    }

    /**
     * 非必填项
     * @param name
     * @param type
     * @param desc
     * @return
     */
    public static TaskNeedParams ofK(String name, Class<?> type, String desc){
        return TaskNeedParams.builder()
                .name(name)
                .defaultValue(null)
                .required(false)
                .desc(desc)
                .type(type)
                .build();
    }

    public static TaskNeedParams ofSelection(String name, String defaultValue, String desc, List<TaskNeedParams> selections){
        return TaskNeedParams.builder()
                .name(name)
                .defaultValue(defaultValue)
                .required(true)
                .desc(desc)
                .type(String.class)
                .selection(selections)
                .build();
    }

    public static TaskNeedParams ofParams(String name, String desc, List<TaskNeedParams> params){
        return TaskNeedParams.builder()
                .name(name)
                .defaultValue(null)
                .required(false)
                .desc(desc)
                .type(String.class)
                .params(params)
                .build();
    }
}
