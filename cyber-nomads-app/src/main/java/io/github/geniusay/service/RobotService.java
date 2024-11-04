package io.github.geniusay.service;

import io.github.geniusay.pojo.DO.RobotDO;
import io.github.geniusay.pojo.DTO.ChangeRobotDTO;
import io.github.geniusay.pojo.DTO.LoadRobotResponseDTO;
import io.github.geniusay.pojo.VO.RobotVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RobotService {
    LoadRobotResponseDTO loadRobot(MultipartFile file);

    List<RobotVO> queryRobot();

    Boolean removeRoobot(Long id);

    Boolean banRoobot(Long id);

    Boolean changeRobot(ChangeRobotDTO robotDTO);

    List<RobotDO> queryVaildRobot();
}
