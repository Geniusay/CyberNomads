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
    private String type;
    private String desc;
    private boolean required;
    private Object defaultValue;

    private List<TaskNeedParams> childParams = new ArrayList<>();
    public TaskNeedParams(String name, String type, String desc) {
        this.name = name;
        this.type = type;
        this.desc = desc;
        this.required = true;
        this.defaultValue = null;
    }

    public TaskNeedParams(String name, String type, String desc, boolean required, Object defaultValue) {
        this.name = name;
        this.type = type;
        this.desc = desc;
        this.required = required;
        this.defaultValue = defaultValue;
    }

    public TaskNeedParams(String name, String type, String desc, boolean required, Object defaultValue, List<TaskNeedParams> childParams) {
        this.name = name;
        this.type = type;
        this.desc = desc;
        this.required = required;
        this.defaultValue = defaultValue;
        this.childParams = childParams;
    }
}
