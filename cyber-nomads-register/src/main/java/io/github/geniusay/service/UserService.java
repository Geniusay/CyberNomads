package io.github.geniusay.service;

import io.github.common.web.Result;
import io.github.geniusay.pojo.DTO.*;
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
    String download();
    BrowserInfoDTO queryBrowser(String browser);
}
