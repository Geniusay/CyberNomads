package io.github.geniusay.pojo.VO;

import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.pojo.DO.RobotDO;
import io.github.geniusay.pojo.DO.TaskDO;
import io.github.geniusay.utils.ConvertorUtil;
import io.github.geniusay.utils.TimeUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.github.geniusay.utils.TaskTranslationUtil.translatePlatform;
import static io.github.geniusay.utils.TaskTranslationUtil.translateTaskType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskVO {

    private String id;
    private String uid;
    private String nickname;
    private String taskName;
    private String platform;
    private String taskType;
    private String taskStatus;
    private List<String> robots;
    private Map<String, Object> params;
    private String platformCnZh;
    private String taskTypeCnZh;
    private String createTime;

    public static TaskVO convertToTaskVO(TaskDO taskDO) {
        Map<String, Object> params = ConvertorUtil.jsonStringToMap(taskDO.getParams());
        List<String> robotIds = ConvertorUtil.stringToListString(taskDO.getRobots());

        return new TaskVO(
                taskDO.getId().toString(),
                taskDO.getUid(),
                taskDO.getNickname(),
                taskDO.getTaskName(),
                taskDO.getPlatform(),
                taskDO.getTaskType(),
                taskDO.getTaskStatus().name(),
                robotIds,
                params,
                translatePlatform(taskDO.getPlatform()),
                translateTaskType(taskDO.getTaskType()),
                TimeUtil.getFormatTimeStr(taskDO.getCreateTime())
        );
    }

    public static TaskVO convert(Task task){
        List<String> collect = task.getRobots().stream().map(robot -> String.valueOf(robot.getId())).collect(Collectors.toList());
        return new TaskVO(task.getId(),task.getUid(),task.getNickname(),task.getTaskName(),task.getPlatform(),task.getTaskType(),task.getTaskStatus().toString(),collect,task.getParams(),translatePlatform(task.getPlatform()),translatePlatform(task.getTaskType()),null);
    }
}

