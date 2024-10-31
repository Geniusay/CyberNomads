package io.github.geniusay.core.supertask.task;

import io.github.geniusay.pojo.Platform;
import io.github.geniusay.core.task.enums.TaskStatus;
import io.github.geniusay.core.task.enums.TaskType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.geniusay.utils.FormatUtil.getMap;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    private String uid;

    private String nickname;

    private String taskName;

    private Platform platform;

    private TaskType taskType;

    private TaskStatus taskStatus;

    private List<RobotWorker> robotWorkers;
    private ConcurrentHashMap<String, Object> dataMap = new ConcurrentHashMap<>();
    private Logger logger;

    // 组装字段
    private List<TaskNeedParams> needParams;

    private Map<String, Object> params;

    private TaskExecute execute;

    private LogHandler logHandler;

    public String getDataVal(String key) {
        return dataMap.get(key).toString();
    }

    public String getParam(String key) {
        return params.get(key).toString();
    }

    public <T> T getParam(String key, Class<T> clazz) {
        return getMap(key, params, clazz);
    }

    public <T> T getDataVal(String key, Class<T> clazz) {
        return getMap(key, dataMap, clazz);
    }

    public void log(String msg, Object... args) {
        try {
            logger.info(msg, args);
        } catch (Exception e) {
            logger = LoggerFactory.getLogger(taskName);
            logger.info(msg, args);
        }
    }
}
