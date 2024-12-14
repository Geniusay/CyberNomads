package io.github.geniusay.core.recommend;

import io.github.geniusay.core.supertask.config.TaskTypeConstant;
import io.github.geniusay.pojo.DO.SharedRobotDO;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.*;

import static io.github.geniusay.constants.RedisConstant.sharedRobotsRecommend;
import static io.github.geniusay.pojo.VO.SharedRobotVO.stringToList;

@Component
public class UsingRecommend implements Recommend{
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final Integer SIZE = 10;
    @Override
    public void init(List<SharedRobotDO> list) {
        Map<String, Set<ZSetOperations.TypedTuple<String>>> map = new HashMap<>();
        List<String> taskTypes = getAllTaskType();
        for (String taskType : taskTypes) {
            map.put(taskType, new HashSet<>());
        }
        for (SharedRobotDO robotDO : list) {
             List<String> types = stringToList(robotDO.getFocusTask());
             for (String type : types) {
//                 map.get(type).add(new DefaultTypedTuple<>());
             }
        }
        for (String taskType : map.keySet()) {
            stringRedisTemplate.opsForZSet().add(sharedRobotsRecommend(taskType), map.get(taskType));
        }
    }

    @Override
    public List<String> recommend(String taskType, Integer page, Integer size) {
        page = page == null || page < 1 ? 1 : page;
        Set<String> range = stringRedisTemplate.opsForZSet().range(sharedRobotsRecommend(taskType), (long) page * SIZE, (long) (page + 1) * SIZE);
        if(Objects.isNull(range) || range.isEmpty()){
            return new ArrayList<>();
        }
        return new ArrayList<>(range);
    }

    @Override
    public void using(String taskType, String robotId) {
        changeScore(taskType, robotId, 1);
    }

    @Override
    public void release(String taskType, String robotId) {
        changeScore(taskType, robotId, -1);
    }

    private void changeScore(String taskType, String robotId, Integer score) {
        stringRedisTemplate.opsForZSet().incrementScore(sharedRobotsRecommend(taskType), robotId, score);
    }

    private List<String> getAllTaskType(){
        List<String> list = new ArrayList<>();
        try {
            for (Field field : TaskTypeConstant.class.getDeclaredFields()) {
                if (field.getType() == String.class) {
                    list.add((String) field.get(null));
                }
            }
        }
         catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
