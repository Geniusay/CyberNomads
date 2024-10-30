package io.github.geniusay.pojo.DTO;

import io.github.geniusay.pojo.DO.RobotDO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/30 12:14
 */
@Data
@Builder
public class LoadRobotResponseDTO {

    private List<String> successRobot;
    private List<String> errorRobot;

}
