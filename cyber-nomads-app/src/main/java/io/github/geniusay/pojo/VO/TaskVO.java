package io.github.geniusay.pojo.VO;

import io.github.geniusay.pojo.DO.TaskDO;
import io.github.geniusay.utils.ConvertorUtil;
import io.github.geniusay.utils.TimeUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

import static io.github.geniusay.utils.TaskTranslationUtil.translatePlatform;
import static io.github.geniusay.utils.TaskTranslationUtil.translateTaskType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskVO {

    private Long id;
    private String uid;
    private String nickname;
    private String taskName;
    private String platform;
    private String taskType;
    private String taskStatus;
    private List<Long> robots;
    private Map<String, Object> params;
    private String platformCnZh;
    private String taskTypeCnZh;
    private String createTime;

    public static TaskVO convertToTaskVO(TaskDO taskDO) {
        Map<String, Object> params = ConvertorUtil.jsonStringToMap(taskDO.getParams());
        List<Long> robotIds = ConvertorUtil.stringToList(taskDO.getRobots());

        return new TaskVO(
                taskDO.getId(),
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
}

