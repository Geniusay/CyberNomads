package io.github.geniusay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.common.web.Result;
import io.github.geniusay.pojo.DO.RobotDO;
import io.github.geniusay.pojo.DO.SharedRobotDO;
import io.github.geniusay.pojo.DTO.*;
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

    Result<?> changeRobotCookie(UpdateCookieDTO updateCookieDTO);

    List<RobotDO> queryVaildRobot();

    Boolean addRobot(AddRobotDTO robotDTO);

    Map<String, String> getCookie(GetCookieDTO getCookieDTO);

    Boolean insertOrUpdateRobot(LoginMachineDTO loginMachineDTO);

    Boolean shareRobot(ShareRobotDTO shareRobotDTO);

    Page<SharedRobotDO> getPage(Integer page, Integer size);
}
