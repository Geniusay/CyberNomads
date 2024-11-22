package io.github.geniusay.core.supertask;

import io.github.geniusay.core.supertask.taskblueprint.AbstractTaskBlueprint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TaskStrategyManager {

    private final Map<String, List<String>> platformBlueprints = new HashMap<>();
    private final Map<String, String> blueprintStrategies = new HashMap<>();

    private final Set<String> allTaskTypes = new TreeSet<>();

    private final ApplicationContext applicationContext;

    @Autowired
    public TaskStrategyManager(ApplicationContext context) {
        this.applicationContext = context;

        Map<String, AbstractTaskBlueprint> blueprintMap = context.getBeansOfType(AbstractTaskBlueprint.class);

        blueprintMap.forEach((name, blueprint) -> {
            String platform = blueprint.platform();
            String taskType = blueprint.taskType();

            platformBlueprints
                    .computeIfAbsent(platform, k -> new ArrayList<>())
                    .add(name);

            blueprintStrategies.put(platform + taskType, name);

            allTaskTypes.add(taskType);
        });
    }

    /**
     * 获取所有支持的平台列表
     *
     * @return 平台列表
     */
    public Set<String> getSupportedPlatforms() {
        return platformBlueprints.keySet();
    }

    /**
     * 根据平台获取该平台的所有任务蓝图
     *
     * @param platform 平台名称
     * @return 该平台的任务蓝图列表
     */
    public List<AbstractTaskBlueprint> getBlueprintsForPlatform(String platform) {
        // 获取该平台所有蓝图的 Bean 名称
        List<String> blueprintNames = platformBlueprints.getOrDefault(platform, Collections.emptyList());

        // 根据 Bean 名称动态获取新的实例
        List<AbstractTaskBlueprint> blueprints = new ArrayList<>();
        for (String name : blueprintNames) {
            blueprints.add(applicationContext.getBean(name, AbstractTaskBlueprint.class));
        }

        return blueprints;
    }

    /**
     * 根据平台和任务类型获取任务蓝图
     *
     * @param platform 平台名称
     * @param type     任务类型
     * @return 新的任务蓝图实例
     */
    public AbstractTaskBlueprint getBlueprint(String platform, String type) {
        String blueprintName = blueprintStrategies.get(platform + type);

        if (blueprintName == null) {
            return null;
        }
        return applicationContext.getBean(blueprintName, AbstractTaskBlueprint.class);
    }

    /**
     * 获取已有的所有任务类型
     *
     * @return 任务集合
     */
    public Set<String> getAllTaskTypes() {
        return this.allTaskTypes;
    }
}