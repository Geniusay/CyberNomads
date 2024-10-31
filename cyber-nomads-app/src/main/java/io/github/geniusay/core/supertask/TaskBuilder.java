package io.github.geniusay.core.supertask;

import io.github.geniusay.core.supertask.task.WelsirRobot;
import io.github.geniusay.pojo.DO.RobotDO;
import io.github.geniusay.pojo.DO.TaskDO;
import io.github.geniusay.pojo.Platform;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.core.supertask.taskblueprint.AbstractTaskBlueprint;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
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
                .welsirRobots(robotToWelsirRobot(taskDO.getRobots()))
                .build();

        BeanUtils.copyProperties(taskDO, task);
        return task;
    }

    public static List<WelsirRobot> robotToWelsirRobot(List<RobotDO> robotDOS){
        List<WelsirRobot> welsirRobots = new ArrayList<>();
        for (RobotDO robotDO : robotDOS) {
            welsirRobots.add(new WelsirRobot(robotDO));
        }
        return welsirRobots;
    }
}
