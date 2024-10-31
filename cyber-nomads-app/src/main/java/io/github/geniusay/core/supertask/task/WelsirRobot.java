package io.github.geniusay.core.supertask.task;

import io.github.geniusay.pojo.DO.RobotDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WelsirRobot {

    private RobotDO robotDO;

    private Task currentTask;

    public WelsirRobot(RobotDO robotDO) {
        this.robotDO = robotDO;
    }

    public RobotDO robot(){
        return robotDO;
    };

    public Task task(){
        return currentTask;
    }
}
