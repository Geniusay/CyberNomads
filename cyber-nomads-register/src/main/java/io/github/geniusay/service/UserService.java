package io.github.geniusay.service;

import io.github.geniusay.pojo.DTO.LoginDTO;
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

    Object queryRobots();

    void saveKey(String scriptKey);

    void removeMachineCode();
}
