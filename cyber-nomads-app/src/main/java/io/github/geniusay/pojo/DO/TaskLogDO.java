package io.github.geniusay.pojo.DO;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@TableName("task_log")
@NoArgsConstructor
@Data
public class TaskLogDO {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private boolean success;

    private String content;

    private String uid;

    private String taskId;

    private String taskName;

    private String robotId;

    private String robotName;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
