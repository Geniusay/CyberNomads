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
import io.github.geniusay.service.UserService;
import io.github.geniusay.utils.*;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/27 21:39
 */
@Service
public class IUserService implements UserService {

    private static final Logger log = LoggerFactory.getLogger(IUserService.class);
    @Resource
    private UserMapper userMapper;
    @Resource
    private ImageUtil imageUtil;
    @Resource
    private AsyncService asyncService;
    @Resource
    private CacheUtil cacheUtil;

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
        if(user == null){
            throw new ServeException("用户未注册");
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
        UserDO user = UserDO.builder()
                .nickname(UserConstant.CYBER_NOMADS + RandomUtil.generateRandomString(8))
                .uid(UUID.randomUUID().toString())
                .avatar("https://geniusserve.oss-cn-shanghai.aliyuncs.com/cybernomads/plant.ico")
                .email(req.getEmail()).point(3)
                .password(DigestUtils.md5Hex(req.getPassword()))
                .build();
        try {
            userMapper.insert(user);
        }catch (Exception e){
            throw new ServeException("用户已注册或注册失败");
        }
        String token = TokenUtil.getToken(user.getUid(), user.getEmail(), user.getNickname());
        return LoginVO.builder().userVO(UserVO.convert(user)).token(token).build();
    }

    @Override
    public Map<String, String> generateCaptcha() {
        String pid = UUID.randomUUID().toString();
        Map<String, String> code = imageUtil.generateCode();
        String seeCode = CyberStringUtils.toLower(code.get("code"));
        log.info("验证码:{}", code);
        cacheUtil.putCaptcha(pid, seeCode);
        return Map.of("base64",code.get("base64"),"pid",pid);
    }

    @Override
    public void generateEmailCode(String email,String pid,String code) {
        if(cacheUtil.emailCodeIsExpired(email)){
            throw new ServeException("验证码冷却中");
        }
        if(!StringUtils.equals(cacheUtil.getCaptchaAndRemove(pid), CyberStringUtils.toLower(code))){
            throw new ServeException("验证码错误");
        }
        String emailCode = RandomUtil.generateRandomString(6);
        cacheUtil.putEmail(email, CyberStringUtils.toLower(emailCode));
        asyncService.sendCodeToEmail(email, emailCode);
    }

    @Override
    public UserVO queryUserByEmail(String email) {
        UserDO userDO = userMapper.selectOne(new QueryWrapper<UserDO>().eq("email", email));
        if(userDO==null)
            throw new ServeException("用户不存在");
        return UserVO.convert(userDO);
    }
}
