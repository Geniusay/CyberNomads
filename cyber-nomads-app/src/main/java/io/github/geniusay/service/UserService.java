package io.github.geniusay.service;

import io.github.geniusay.pojo.DTO.LoginRequestDTO;
import io.github.geniusay.pojo.VO.LoginVO;
import io.github.geniusay.pojo.DTO.RegisterRequestDTO;
import io.github.geniusay.pojo.VO.UserVO;

import java.util.Map;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/27 21:40
 */
public interface UserService {

    UserVO queryUserById(String uid);

    LoginVO login(LoginRequestDTO req);

    LoginVO register(RegisterRequestDTO req);

    Map<String,String> generateCaptcha();

    void generateEmailCode(String email,String pid,String code);
}
