package io.github.geniusay.core.supertask.task;

import io.github.geniusay.core.supertask.TaskLogProcessor;
import io.github.geniusay.core.supertask.config.TaskStatus;
import io.github.geniusay.core.supertask.plugin.terminator.Terminator;
import io.github.geniusay.pojo.DO.RobotDO;
import io.github.geniusay.pojo.DO.TaskDO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;


import static io.github.geniusay.core.supertask.config.TaskConstant.ERROR_CODE;
import static io.github.geniusay.core.supertask.config.TaskConstant.ERROR_MESSAGE;
import static io.github.geniusay.utils.ConvertorUtil.getMap;


@Data
@NoArgsConstructor
public class Task {

    private String id;

    private String uid;

    private String nickname;

    private String taskName;

    private String platform;

    private String taskType;

    private TaskStatus taskStatus;

    private List<RobotDO> robots;

    private ConcurrentHashMap<String, Object> dataMap;

    // 组装字段
    private List<TaskNeedParams> needParams;

    private Map<String, Object> params;

    private TaskExecute execute;

    private LastWordHandler lastWord;

    private Logger logger;

    private Terminator terminator;

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

    public void log(String msg,Object...args){
        this.logger.info(msg,args);
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

    private Task(List<RobotDO> robots, ConcurrentHashMap<String, Object> dataMap, List<TaskNeedParams> needParams, Map<String, Object> params, TaskExecute execute, LastWordHandler lastWord) {
        this.robots = robots;
        this.dataMap = dataMap;
        this.needParams = needParams;
        this.params = params;
        this.execute = execute;
        this.lastWord = lastWord;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{

        private List<RobotDO> robots = new ArrayList<>();

        private ConcurrentHashMap<String, Object> dataMap = new ConcurrentHashMap<>();

        // 组装字段
        private List<TaskNeedParams> needParams = new ArrayList<>();

        private Map<String, Object> params = new HashMap<>();

        private TaskExecute execute;

        private LastWordHandler lastWord;

        private TaskDO taskDO;

        public Builder execute(TaskExecute execute){
            this.execute = execute;
            return this;
        }

        public Builder lastWord(LastWordHandler lastWordHandler){
            this.lastWord = lastWordHandler;
            return this;
        }

        public Builder taskDO(TaskDO taskDO){
            this.taskDO = taskDO;
            return this;
        }

        public Builder needParams(List<TaskNeedParams> needParams){
            this.needParams = needParams;
            return this;
        }

        public Task build(){
            Task task = new Task(this.robots, this.dataMap, this.needParams, this.params, this.execute, this.lastWord);
            BeanUtils.copyProperties(taskDO, task);
            task.getRobots().addAll(taskDO.getRobotList()==null?new ArrayList<>():taskDO.getRobotList());
            task.setLogger(LoggerFactory.getLogger(task.getTaskName()));
            return task;
        }
    }

    public boolean inStatusList(TaskStatus ...statues){
        return Stream.of(statues).anyMatch(status-> status.equals(this.taskStatus));
    }
}
