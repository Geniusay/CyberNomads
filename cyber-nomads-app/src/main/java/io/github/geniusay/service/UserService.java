package io.github.geniusay.service;

import io.github.common.web.Result;
import io.github.geniusay.pojo.DTO.LoadRobotResponseDTO;
import io.github.geniusay.pojo.DTO.LoginRequestDTO;
import io.github.geniusay.pojo.VO.LoginVO;
import io.github.geniusay.pojo.DTO.RegisterRequestDTO;
import io.github.geniusay.pojo.VO.RobotVO;
import io.github.geniusay.pojo.VO.UserVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/27 21:40
 */
public interface UserService {

    UserVO queryUserById(String uid);

    Result<UserVO> getUserInfo();

    LoginVO login(LoginRequestDTO req);

    LoginVO register(RegisterRequestDTO req);

    Map<String,String> generateCaptcha();

    void generateEmailCode(String email,String pid,String code);
}
