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
}
