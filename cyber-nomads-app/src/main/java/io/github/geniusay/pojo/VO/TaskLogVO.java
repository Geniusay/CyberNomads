package io.github.geniusay.pojo.VO;

import io.github.geniusay.pojo.DO.TaskLogDO;
import lombok.Data;

import java.util.Date;

@Data
public class TaskLogVO {

    private String id;
    private boolean success;
    private String content;
    private String robotName;
    private Date createTime;

    public static TaskLogVO convertToTaskLogVO(TaskLogDO taskLogDO) {
        TaskLogVO taskLogVO = new TaskLogVO();
        taskLogVO.setId(String.valueOf(taskLogDO.getId()));
        taskLogVO.setSuccess(taskLogDO.isSuccess());
        taskLogVO.setContent(taskLogDO.getContent());
        taskLogVO.setRobotName(taskLogDO.getRobotName());
        taskLogVO.setCreateTime(taskLogDO.getCreateTime());
        return taskLogVO;
    }
}