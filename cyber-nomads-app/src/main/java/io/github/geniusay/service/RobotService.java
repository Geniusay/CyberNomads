package io.github.geniusay.service;

import io.github.common.web.Result;
import io.github.geniusay.pojo.DO.RobotDO;
import io.github.geniusay.pojo.DTO.AddRobotDTO;
import io.github.geniusay.pojo.DTO.ChangeRobotDTO;
import io.github.geniusay.pojo.DTO.GetCookieDTO;
import io.github.geniusay.pojo.DTO.LoadRobotResponseDTO;
import io.github.geniusay.pojo.VO.PlatformVO;
import io.github.geniusay.pojo.VO.RobotVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface RobotService {
    LoadRobotResponseDTO loadRobot(MultipartFile file);

    List<RobotVO> queryRobot();

    Boolean removeRoobot(Long id);

    Boolean banRoobot(Long id);

    Boolean changeRobot(ChangeRobotDTO robotDTO);

    List<RobotDO> queryVaildRobot();

    Result<?> getPlatforms();

    Boolean addRobot(AddRobotDTO robotDTO);

    Map<String, String> getCookie(GetCookieDTO getCookieDTO);
}
