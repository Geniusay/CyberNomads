package io.github.geniusay.service;

import com.mysql.cj.log.Log;
import io.github.geniusay.pojo.VO.LoginRequestVO;
import io.github.geniusay.pojo.VO.LoginVO;
import io.github.geniusay.pojo.VO.RegisterRequestVO;
import io.github.geniusay.pojo.VO.UserVO;
import org.apache.catalina.User;

import java.util.List;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/27 21:40
 */
public interface UserService {

    List<UserVO> queryUser();

    UserVO queryUserById(Integer uid);

    LoginVO login(LoginRequestVO req);

    LoginVO register(RegisterRequestVO req);


}
