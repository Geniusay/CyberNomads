package io.github.geniusay.pojo.VO;

import io.github.geniusay.core.supertask.config.TaskStatus;
import lombok.Data;

import java.util.List;

@Data
public class TaskLogResponse {

    private TaskStatus taskStatus;
    private List<TaskLogVO> taskLogs;

    public TaskLogResponse(TaskStatus taskStatus, List<TaskLogVO> taskLogs) {
        this.taskStatus = taskStatus;
        this.taskLogs = taskLogs;
    }
}