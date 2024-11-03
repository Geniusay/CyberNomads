package io.github.geniusay.pojo.DO;

import com.baomidou.mybatisplus.annotation.*;
import io.github.geniusay.core.supertask.config.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("task")
public class TaskDO {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String uid;

    private String nickname;

    private String taskName;

    private String platform;

    private String taskType;

    private TaskStatus taskStatus;

    private String robots;

    private String params;

    @TableField(exist = false)
    private List<RobotDO> robotList;
}
