package io.github.geniusay.core.supertask;

import io.github.geniusay.pojo.DO.TaskDO;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.core.supertask.taskblueprint.AbstractTaskBlueprint;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

@Component
public class TaskBuilder {

    @Resource
    TaskStrategyManager strategyMan;

    @Resource
    RobotWorkerFactory robotWorkerFactory;

    public Task buildTask(TaskDO taskDO, String platform, String type) {
        AbstractTaskBlueprint blueprint = strategyMan.getBlueprint(platform, type);
        if (Objects.isNull(blueprint)) {
            throw new RuntimeException(String.format("%s is not exist", platform + type));
        }
        Task task = Task.builder()
                .execute(blueprint.supplierExecute())
                .logHandler(blueprint.supplierLog())
                .needParams(blueprint.supplierNeedParams())
                .robotWorkers(robotWorkerFactory.createRobotWorkers(taskDO.getRobots()))
                .build();

        BeanUtils.copyProperties(taskDO, task);
        return task;
    }
}
