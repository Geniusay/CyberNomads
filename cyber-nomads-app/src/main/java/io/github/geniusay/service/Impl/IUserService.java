package io.github.geniusay.service.Impl;

import io.github.geniusay.mapper.UserMapper;
import io.github.geniusay.pojo.DTO.UserDTO;
import io.github.geniusay.pojo.VO.LoginRequestVO;
import io.github.geniusay.pojo.VO.LoginVO;
import io.github.geniusay.pojo.VO.RegisterRequestVO;
import io.github.geniusay.pojo.VO.UserVO;
import io.github.geniusay.service.UserService;
import io.github.geniusay.utils.CacheUtil;
import io.github.geniusay.utils.TokenUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/27 21:39
 */
@Service
public class IUserService implements UserService {

    @Resource
    UserMapper userMapper;

    @Override
    public List<UserVO> queryUser() {
        List<UserDTO> list = userMapper.selectList(null);
        return list.stream()
                .map(UserVO::convert)
                .collect(Collectors.toList());
    }

    @Override
    public UserVO queryUserById(Integer id) {
        UserDTO dto = userMapper.selectById(id);
        return UserVO.convert(dto);
    }

    @Override
    public LoginVO login(LoginRequestVO req) {
        String pid = req.getPid();
        String code = req.getCode();

        if(!Objects.equals(code,null)&&!Objects.equals(pid,null)){
            throw new IllegalArgumentException("验证码错误");
        }

        if(!Objects.equals(CacheUtil.getCaptchaAndRemove(pid), code)){
            throw new IllegalArgumentException("验证码错误");
        }

        List<UserDTO> user = userMapper.selectByMap(Map.of("email", req.getEmail(), "password", req.getPassword()));
        if(user.size()==0){
            throw new IllegalArgumentException("账号密码错误");
        }
        String token = TokenUtil.getToken(String.valueOf(user.get(0).getId()), user.get(0).getEmail());
        return LoginVO.builder().userVO(UserVO.convert(user.get(0))).token(token).build();
    }


    @Override
    public LoginVO register(RegisterRequestVO req) {
        String code = req.getCode();
        String cacheCode = CacheUtil.getEmailAndRemove(req.getEmail());
        if(code==null&&!cacheCode.equals(cacheCode)){
            throw new IllegalArgumentException("验证码错误");
        }
        UserDTO user = UserDTO.builder()
                .nickName("")
                .avator("https://geniusserve.oss-cn-shanghai.aliyuncs.com/cybernomads/plant.ico")
                .email(req.getEmail()).point(3)
                .build();
        userMapper.insert(user);
        String token = TokenUtil.getToken(String.valueOf(user.getId()), user.getEmail());
        return LoginVO.builder().userVO(UserVO.convert(user)).token(token).build();
    }


}
