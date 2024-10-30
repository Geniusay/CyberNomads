package io.github.geniusay.supertask.task;

import io.github.geniusay.pojo.DO.RobotDO;
import io.github.geniusay.pojo.Platform;
import io.github.geniusay.task.enums.TaskStatus;
import io.github.geniusay.task.enums.TaskType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Task{

    private String uid;

    private String nickname;

    private String taskName;

    private Platform platform;

    private TaskType taskType;

    private TaskStatus taskStatus;

    private List<RobotDO> robots;

    private ConcurrentHashMap<String,Object> dataMap;

    // 组装字段
    private List<TaskHelpParams> helpParams;

    private Map<String, Object> params;

    private TaskExecute handler;

    private LogHandler logHandler;

    public Object getDataVal(String key){
        return dataMap.get("key");
    }

}
