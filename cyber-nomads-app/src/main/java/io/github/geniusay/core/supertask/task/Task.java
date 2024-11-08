package io.github.geniusay.core.supertask.task;

import io.github.geniusay.core.supertask.config.TaskStatus;
import io.github.geniusay.core.supertask.plugin.terminator.AbstractTerminator;
import io.github.geniusay.core.supertask.plugin.terminator.Terminator;
import io.github.geniusay.pojo.DO.LastWord;
import io.github.geniusay.pojo.DO.RobotDO;
import io.github.geniusay.pojo.DO.TaskDO;
import io.github.geniusay.pojo.DO.TaskLogDO;
import io.github.geniusay.utils.ConvertorUtil;
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


import static io.github.geniusay.core.supertask.config.TaskConstant.*;
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

    private AbstractTerminator terminator;

    private List<TaskLogDO> loglist;

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
     * 添加遗言到 dataMap 中
     */
    public void addLastWord(RobotWorker robot, LastWord lastWord) {
        String key = robot.getId() + LAST_WORD;
        dataMap.put(key, lastWord);
    }

    /**
     * 从 dataMap 中获取与 robot 相关的 LastWord 执行结果
     */
    public LastWord getLastWord(RobotWorker robot) {
        String key = robot.getId() + LAST_WORD;
        return (LastWord) dataMap.get(key);
    }

    private Task(List<RobotDO> robots, ConcurrentHashMap<String, Object> dataMap, List<TaskNeedParams> needParams, Map<String, Object> params, TaskExecute execute, LastWordHandler lastWord, List<TaskLogDO> loglist) {
        this.robots = robots;
        this.dataMap = dataMap;
        this.needParams = needParams;
        this.params = params;
        this.execute = execute;
        this.lastWord = lastWord;
        this.loglist = loglist;
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


        private List<TaskLogDO> loglist = new ArrayList<>();

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
            Task task = new Task(this.robots, this.dataMap, this.needParams, this.params, this.execute, this.lastWord, this.loglist);
            BeanUtils.copyProperties(taskDO, task);
            task.getRobots().addAll(taskDO.getRobotList()==null?new ArrayList<>():taskDO.getRobotList());
            task.setLogger(LoggerFactory.getLogger(task.getTaskName()));
            task.params = ConvertorUtil.jsonStringToMap(taskDO.getParams());
            task.setId(String.valueOf(taskDO.getId()));
            return task;
        }
    }

    public boolean inStatusList(TaskStatus ...statues){
        return Stream.of(statues).anyMatch(status-> status.equals(this.taskStatus));
    }
}
