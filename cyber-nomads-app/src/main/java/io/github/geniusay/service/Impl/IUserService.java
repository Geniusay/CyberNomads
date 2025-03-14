package io.github.geniusay.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.common.web.Result;
import io.github.geniusay.constants.UserConstant;
import io.github.geniusay.core.async.AsyncService;
import io.github.geniusay.core.exception.ServeException;
import io.github.geniusay.mapper.UserMapper;
import io.github.geniusay.pojo.DO.UserDO;
import io.github.geniusay.pojo.DTO.*;
import io.github.geniusay.pojo.VO.LoginVO;
import io.github.geniusay.pojo.VO.UserVO;
import io.github.geniusay.service.TaskService;
import io.github.geniusay.service.UserService;
import io.github.geniusay.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

import static io.github.geniusay.constants.UserConstant.*;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/27 21:39
 */
@Slf4j
@Service
public class IUserService implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private ImageUtil imageUtil;
    @Resource
    private AsyncService asyncService;
    @Resource
    private CacheUtil cacheUtil;
    @Resource
    private TaskService taskService;

    @Override
    public UserVO queryUserById(String uid) {
        UserDO dto = userMapper.selectOne(new QueryWrapper<UserDO>().eq("uid",uid));
        if(dto==null){
            throw new ServeException("用户不存在");
        }
        return UserVO.convert(dto);
    }

    @Override
    public Result<UserVO> getUserInfo() {
        String uid = ThreadUtil.getUid();
        UserDO dto = userMapper.selectOne(new QueryWrapper<UserDO>().eq("uid",uid));
        return Result.success(UserVO.convert(dto));
    }

    @Override
    public LoginVO login(LoginRequestDTO req) {
        String code = req.getCode();
        String emailCode = cacheUtil.getEmailAndRemove(req.getEmail());
        if(!StringUtils.equals(CyberStringUtils.toLower(code), emailCode)){
            throw new ServeException("验证码错误");
        }

        UserDO user = userMapper.selectOne(new QueryWrapper<UserDO>()
                .eq("email",req.getEmail()));
        // 用户不存在则注册
        if(user == null){
            user = addNewUser(req.getEmail());
        }
        String token = TokenUtil.getToken(String.valueOf(user.getUid()), user.getEmail(), user.getNickname());
        return LoginVO.builder().userVO(UserVO.convert(user)).token(token).build();
    }

    @Override
    public void logout() {
        TokenUtil.logout(ThreadUtil.getUid(), ThreadUtil.getEmail(), ThreadUtil.getNickname());
    }

    @Override
    public LoginVO register(RegisterRequestDTO req) {
        String code = req.getCode();
        String cacheCode = cacheUtil.getEmailAndRemove(req.getEmail());
        if(!code.toLowerCase().equals(cacheCode)){
            throw new ServeException("验证码错误");
        }
        UserDO user = addNewUser(req.getEmail());
        String token = TokenUtil.getToken(user.getUid(), user.getEmail(), user.getNickname());
        return LoginVO.builder().userVO(UserVO.convert(user)).token(token).build();
    }

    private UserDO addNewUser(String email) {
        UserDO user = UserDO.builder()
                .nickname(UserConstant.CYBER_NOMADS + RandomUtil.generateRandomString(8))
                .uid(UUID.randomUUID().toString())
                .avatar("https://geniusserve.oss-cn-shanghai.aliyuncs.com/cybernomads/plant.ico")
                .email(email).point(3)
                .password(DigestUtils.md5Hex(DEFAULT_PWD))
                .build();
        try {
            userMapper.insert(user);
        }catch (Exception e){
            throw new ServeException("请勿频繁操作!");
        }
        return user;
    }

    @Override
    public Map<String, String> generateCaptcha() {
        String pid = UUID.randomUUID().toString();
        Map<String, String> code = imageUtil.generateCode();
        String seeCode = CyberStringUtils.toLower(code.get("code"));
        cacheUtil.putCaptcha(pid, seeCode);
        return Map.of("base64",code.get("base64"),"pid",pid);
    }

    @Override
    public Result<?> generateEmailCode(String email,String pid,String code) {
        if(cacheUtil.emailCodeIsExpired(email)){
            throw new ServeException("验证码冷却中");
        }
        if(!StringUtils.equals(cacheUtil.getCaptchaAndRemove(pid), CyberStringUtils.toLower(code))){
            throw new ServeException("验证码错误");
        }
        String emailCode = RandomUtil.generateRandomString(6);
        cacheUtil.putEmail(email, CyberStringUtils.toLower(emailCode));
        asyncService.sendCodeToEmail(email, emailCode);
        return Result.success(
                Map.of("email", email, "coolDown",EMAIL_COOL_DOWN)
        );
    }

    @Override
    public UserVO queryUserByEmail(String email) {
        UserDO userDO = userMapper.selectOne(new QueryWrapper<UserDO>().eq("email", email));
        if(userDO==null)
            throw new ServeException("用户不存在");
        return UserVO.convert(userDO);
    }

    @Override
    public void newcomerPack(UserDO userDO) {
        ThreadUtil.set("uid",userDO.getUid());
        ThreadUtil.set("email",userDO.getEmail());
        ThreadUtil.set("nickname",userDO.getNickname());

        taskService.createTask(UserConstant.newcomerPack);
    }
}
