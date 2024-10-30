package io.github.geniusay.supertask;

import io.github.geniusay.pojo.DO.TaskDO;
import io.github.geniusay.pojo.Platform;
import io.github.geniusay.supertask.task.Task;
import io.github.geniusay.supertask.taskblueprint.AbstractTaskBlueprint;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

@Component
public class TaskBuilder {

    @Resource
    TaskStrategyMan strategyMan;

    public Task buildTask(TaskDO taskDO, Platform platform, String type){
        AbstractTaskBlueprint blueprint = strategyMan.getBlueprint(platform, type);
        if(Objects.isNull(blueprint)){
           throw new RuntimeException(String.format("%s is not exist", platform+type));
        }
        Task task = Task.builder()
                .handler(blueprint.supplierExecute())
                .logHandler(blueprint.supplierLog())
                .helpParams(blueprint.supplierHelpParams())
                .build();

        BeanUtils.copyProperties(taskDO, task);
        return task;
    }
}
