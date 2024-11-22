package io.github.geniusay.core.supertask;

import io.github.geniusay.core.supertask.plugin.TaskPluginFactory;
import io.github.geniusay.core.supertask.plugin.terminator.AbstractTerminator;
import io.github.geniusay.pojo.DO.TaskDO;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.core.supertask.taskblueprint.AbstractTaskBlueprint;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

import static io.github.geniusay.core.supertask.config.PluginConstant.TERMINATOR_GROUP_NAME;

@Component
public class TaskFactory {

    @Resource
    TaskStrategyManager strategyManager;

    @Resource
    TaskPluginFactory taskPluginFactory;

    public Task buildTask(TaskDO taskDO, String platform, String type) {
        AbstractTaskBlueprint blueprint = strategyManager.getBlueprint(platform, type);
        if (Objects.isNull(blueprint)) {
            throw new RuntimeException(String.format("%s is not exist", platform + type));
        }
        Task task = Task.builder()
                .execute(blueprint.supplierExecute())
                .needParams(blueprint.supplierNeedParams())
                .lastWord(blueprint.supplierLastWordHandler())
                .taskDO(taskDO)
                .build();

        // 创建任务并注入终结器
        task.setTerminator(taskPluginFactory.<AbstractTerminator>buildPluginWithGroup(TERMINATOR_GROUP_NAME, task));

        // 初始化蓝图
        blueprint.initBlueprint(task);

        return task;
    }
}
