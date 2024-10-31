package io.github.geniusay.core.supertask;


import io.github.geniusay.pojo.Platform;
import io.github.geniusay.core.supertask.taskblueprint.AbstractTaskBlueprint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TaskStrategyMan {

    private final Map<Platform, List<AbstractTaskBlueprint>> platformTaskBlueprintList;

    private final Map<String, AbstractTaskBlueprint> blueprintStrategy;

    @Autowired
    public TaskStrategyMan(ApplicationContext context) {
        platformTaskBlueprintList = new HashMap<>();
        blueprintStrategy = new HashMap<>();
        Map<String, AbstractTaskBlueprint> blueprintMap = context.getBeansOfType(AbstractTaskBlueprint.class);
        blueprintMap.forEach(
                (k,blueprint)->{
                    Platform platform = blueprint.platform();
                    if (!platformTaskBlueprintList.containsKey(platform)) {
                        platformTaskBlueprintList.put(blueprint.platform(), new ArrayList<>());
                    }
                    platformTaskBlueprintList.get(blueprint.platform()).add(blueprint);
                    blueprintStrategy.put(platform.getPlat()+blueprint.taskType(), blueprint);
                }
        );
    }

    public List<AbstractTaskBlueprint> getBlueprintList(Platform platform){
        return platformTaskBlueprintList.getOrDefault(platform, new ArrayList<>());
    }

    public AbstractTaskBlueprint getBlueprint(Platform platform, String type){
        return blueprintStrategy.get(platform.getPlat()+type);
    }
}
