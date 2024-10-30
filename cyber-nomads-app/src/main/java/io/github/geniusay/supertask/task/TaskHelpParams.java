package io.github.geniusay.supertask.task;


import lombok.Data;

/**
 * 提供给前端要输入的字段
 */
@Data
public class TaskHelpParams {
    private String paramName;
    private String paramType;
    private String paramDesc;

    public TaskHelpParams(String paramName, String paramType) {
        this.paramName = paramName;
        this.paramType = paramType;
    }

    public TaskHelpParams(String paramName, String paramType, String paramDesc) {
        this.paramName = paramName;
        this.paramType = paramType;
        this.paramDesc = paramDesc;
    }
}
