package io.github.geniusay.core.supertask;

import io.github.geniusay.core.supertask.taskblueprint.AbstractTaskBlueprint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TaskStrategyManager {

    private final Map<String, List<AbstractTaskBlueprint>> platformBlueprints = new HashMap<>();
    private final Map<String, AbstractTaskBlueprint> blueprintStrategies = new HashMap<>();

    @Autowired
    public TaskStrategyManager(ApplicationContext context) {
        Map<String, AbstractTaskBlueprint> blueprintMap = context.getBeansOfType(AbstractTaskBlueprint.class);
        blueprintMap.forEach((name, blueprint) -> {
            String platform = blueprint.platform();
            platformBlueprints
                    .computeIfAbsent(platform, k -> new ArrayList<>())
                    .add(blueprint);
            blueprintStrategies.put(platform + blueprint.taskType(), blueprint);
        });
    }

    /**
     * 获取所有支持的平台列表
     * @return 平台列表
     */
    public Set<String> getSupportedPlatforms() {
        return platformBlueprints.keySet();
    }

    /**
     * 根据平台获取该平台的所有任务蓝图
     * @param platform 平台名称
     * @return 该平台的任务蓝图列表
     */
    public List<AbstractTaskBlueprint> getBlueprintsForPlatform(String platform) {
        return platformBlueprints.getOrDefault(platform, Collections.emptyList());
    }

    /**
     * 根据平台和任务类型获取任务蓝图
     * @param platform 平台名称
     * @param type 任务类型
     * @return 任务蓝图
     */
    public AbstractTaskBlueprint getBlueprint(String platform, String type) {
        return blueprintStrategies.get(platform + type);
    }
}
