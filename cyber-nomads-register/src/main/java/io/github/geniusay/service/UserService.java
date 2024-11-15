package io.github.geniusay.service;

import io.github.common.web.Result;
import io.github.geniusay.pojo.DTO.DriverPathDTO;
import io.github.geniusay.pojo.DTO.LoginDTO;
import io.github.geniusay.pojo.DTO.QueryPathDTO;
import io.github.geniusay.pojo.DTO.VerityDTO;
import io.github.geniusay.pojo.VO.RobotVO;

import java.util.List;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/11 14:15
 */
public interface UserService {

    //登录账号
    Boolean login(LoginDTO loginDTO);

    Result queryRobots();

    void saveKey(String scriptKey);

    void removeMachineCode();

    Boolean verityCode(String code);

    Boolean verityPath(DriverPathDTO pathDTO);
    QueryPathDTO queryPathExist();
}
