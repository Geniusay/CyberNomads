package io.github.geniusay.core.recommend;

import io.github.geniusay.pojo.DO.SharedRobotDO;

import java.util.List;

public interface Recommend {
    void init(List<SharedRobotDO> list);

    List<String> recommend(String taskType, Integer page, Integer size);

    void using(String taskType, String robotId);

    void release(String taskType, String robotId);
}
