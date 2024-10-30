package io.github.geniusay.supertask.task;

import io.github.geniusay.pojo.Platform;
import io.github.geniusay.task.enums.TaskStatus;
import io.github.geniusay.task.enums.TaskType;
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


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task{

    private String uid;

    private String nickname;

    private String taskName;

    private Platform platform;

    private TaskType taskType;

    private TaskStatus taskStatus;

    private List<WelsirRobot> welsirRobots;
    private ConcurrentHashMap<String,Object> dataMap = new ConcurrentHashMap<>();
    private Logger logger;

    // 组装字段
    private List<TaskHelpParams> helpParams;

    private Map<String, Object> params;

    private TaskExecute handler;

    private LogHandler logHandler;

    public Object getDataVal(String key){
        return dataMap.get(key);
    }

    public Object getParams(String key){
        return params.get(key);
    }

    public <T> T getParams(String key, Class<T> clazz){
        return getMap(key, params, clazz);
    }

    public <T> T getDataVal(String key, Class<T> clazz){
        return getMap(key, dataMap, clazz);
    }

    private <T> T getMap(String key, Map<String,Object> map, Class<T> clazz){
        Object data = map.get(key);
        if(!Objects.isNull(data)){
            return clazz.cast(data);
        }
        return null;
    }
    public void log(String msg,Object...args){
        try {
            logger.info(msg,args);
        }catch (Exception e){
            logger = LoggerFactory.getLogger(taskName);
            logger.info(msg,args);
        }

    }

}
