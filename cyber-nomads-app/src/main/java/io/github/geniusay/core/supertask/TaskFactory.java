package io.github.geniusay.core.supertask;

import io.github.geniusay.core.supertask.plugin.terminator.Terminator;
import io.github.geniusay.pojo.DO.TaskDO;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.core.supertask.taskblueprint.AbstractTaskBlueprint;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

@Component
public class TaskFactory {

    @Resource
    TaskStrategyManager strategyManager;

    @Resource
    TerminatorFactory terminatorFactory;

    public Task buildTask(TaskDO taskDO, String platform, String type) {
        AbstractTaskBlueprint blueprint = strategyManager.getBlueprint(platform, type);
        if (Objects.isNull(blueprint)) {
            throw new RuntimeException(String.format("%s is not exist", platform + type));
        }

        Terminator terminator = terminatorFactory.createTerminator(taskDO);

        // 创建任务并注入终结器
        return Task.builder()
                .execute(blueprint.supplierExecute())
                .needParams(blueprint.supplierNeedParams())
                .lastWord(blueprint.supplierLastWordHandler())
                .taskDO(taskDO)
                .terminator(terminator)
                .build();
    }
}
