package io.github.geniusay.pojo.DTO;

import io.github.geniusay.core.supertask.task.TaskNeedParams;
import lombok.Data;

import java.util.List;

/**
 * 用于返回任务类型及其对应的参数列表
 */
@Data
public class TaskFunctionDTO {

    private String taskTypeKey;   // 任务类型的英文标识
    private String taskTypeValue; // 任务类型的中文名称
    private List<TaskNeedParams> params; // 该任务类型对应的参数列表

    public TaskFunctionDTO(String taskTypeKey, String taskTypeValue, List<TaskNeedParams> params) {
        this.taskTypeKey = taskTypeKey;
        this.taskTypeValue = taskTypeValue;
        this.params = params;
    }
}