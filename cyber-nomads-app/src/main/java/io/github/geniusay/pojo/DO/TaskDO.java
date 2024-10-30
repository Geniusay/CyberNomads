package io.github.geniusay.pojo.DO;

import com.baomidou.mybatisplus.annotation.TableName;
import io.github.geniusay.pojo.Platform;
import io.github.geniusay.task.enums.TaskStatus;
import io.github.geniusay.task.enums.TaskType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("") //TODO
public class TaskDO {

    private String uid;

    private String nickname;

    private String taskName;

    private Platform platform;

    private TaskType taskType;

    private TaskStatus taskStatus;

    private List<RobotDO> robots;
}
