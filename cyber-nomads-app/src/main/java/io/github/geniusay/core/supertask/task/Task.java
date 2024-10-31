package io.github.geniusay.core.supertask.task;

import io.github.geniusay.core.supertask.TaskLogProcessor;
import io.github.geniusay.core.supertask.config.TaskStatus;
import io.github.geniusay.pojo.DO.RobotDO;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.geniusay.core.supertask.config.TaskConstant.ERROR_CODE;
import static io.github.geniusay.core.supertask.config.TaskConstant.ERROR_MESSAGE;
import static io.github.geniusay.utils.ConvertorUtil.getMap;

@Data
@Builder
@NoArgsConstructor
public class Task {

    private String uid;

    private String nickname;

    private String taskName;

    private String platform;

    private String taskType;

    private TaskStatus taskStatus;

    private List<RobotDO> robots;

    private Map<String, Object> params;

    // 组装字段
    private TaskExecute execute;

    private List<TaskNeedParams> needParams;

    private ConcurrentHashMap<String, Object> dataMap;

    public String getDataVal(String key) {
        return dataMap.get(key).toString();
    }

    public String getParam(String key) {
        return params.get(key).toString();
    }

    public <T> T getParam(String key, Class<T> clazz) {
        return getMap(key, params, clazz);
    }

    public <T> T getDataValOrDefault(String key, Class<T> clazz, T defaultValue) {
        Object value = dataMap.get(key);
        return value != null ? getMap(key, dataMap, clazz) : defaultValue;
    }

    /**
     * 更新任务状态，并记录状态变化日志
     */
    public void updateStatus(TaskStatus newStatus, TaskLogProcessor logProcessor, String logMessage) {
        this.taskStatus = newStatus;
        logProcessor.addLogToTask(this, logMessage);
    }

    /**
     * 添加错误状态码和错误信息
     */
    public void addErrorCode(String errorCode, String errorMessage) {
        dataMap.put(ERROR_CODE, errorCode);
        dataMap.put(ERROR_MESSAGE, errorMessage);
    }

    public Task(String uid, String nickname, String taskName, String platform, String taskType, TaskStatus taskStatus, List<RobotDO> robots, Map<String, Object> params, TaskExecute execute, List<TaskNeedParams> needParams, ConcurrentHashMap<String, Object> dataMap) {
        this.uid = uid;
        this.nickname = nickname;
        this.taskName = taskName;
        this.platform = platform;
        this.taskType = taskType;
        this.taskStatus = taskStatus;
        this.robots = robots;
        this.params = params;
        this.execute = execute;
        this.needParams = needParams;
        this.dataMap = new ConcurrentHashMap<>();
    }
}
