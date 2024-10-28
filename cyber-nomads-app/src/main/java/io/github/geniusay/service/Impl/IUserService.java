package io.github.geniusay.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.geniusay.async.AsyncService;
import io.github.geniusay.mapper.UserMapper;
import io.github.geniusay.pojo.DO.UserDO;
import io.github.geniusay.pojo.DTO.LoginRequestDTO;
import io.github.geniusay.pojo.VO.LoginVO;
import io.github.geniusay.pojo.DTO.RegisterRequestDTO;
import io.github.geniusay.pojo.VO.UserVO;
import io.github.geniusay.service.UserService;
import io.github.geniusay.utils.CacheUtil;
import io.github.geniusay.utils.ImageUtil;
import io.github.geniusay.utils.RandomUtil;
import io.github.geniusay.utils.TokenUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/27 21:39
 */
@Service
public class IUserService implements UserService {

    @Resource
    UserMapper userMapper;
    @Resource
    ImageUtil imageUtil;
    @Resource
    private AsyncService asyncService;

    @Override
    public UserVO queryUserById(String uid) {

        UserDO dto = userMapper.selectOne(new QueryWrapper<UserDO>().eq("uid",uid));
        Assert.notNull(dto,"用户不存在");
        return UserVO.convert(dto);
    }

    @Override
    public LoginVO login(LoginRequestDTO req) {
        String code = req.getCode();

        if(!Objects.equals(CacheUtil.getEmailAndRemove(req.getEmail()), code)){
            throw new IllegalArgumentException("验证码错误");
        }

        UserDO user = userMapper.selectOne(new QueryWrapper<UserDO>()
                .eq("email",req.getEmail()));
        Assert.notNull(user,"账号密码错误或用户不存在");
        String token = TokenUtil.getToken(String.valueOf(user.getId()), user.getEmail());
        CacheUtil.tokenCache.put(token,user.getUid());
        return LoginVO.builder().userVO(UserVO.convert(user)).token(token).build();
    }

    @Override
    public LoginVO register(RegisterRequestDTO req) {
        String code = req.getCode();
        String cacheCode = CacheUtil.getEmailAndRemove(req.getEmail());
        if(!code.equals(cacheCode)){
            throw new IllegalArgumentException("验证码错误");
        }
        UserDO user = UserDO.builder()
                .nickname("")
                .uid(UUID.randomUUID().toString())
                .avator("https://geniusserve.oss-cn-shanghai.aliyuncs.com/cybernomads/plant.ico")
                .email(req.getEmail()).point(3)
                .password(DigestUtils.md5Hex(req.getPassword()))
                .build();
        userMapper.insert(user);
        String token = TokenUtil.getToken(user.getUid(), user.getEmail());
        CacheUtil.tokenCache.put(token,user.getUid());
        return LoginVO.builder().userVO(UserVO.convert(user)).token(token).build();
    }

    @Override
    public Map<String, String> generateCaptcha() {
        String pid = UUID.randomUUID().toString();
        Map<String, String> code = imageUtil.generateCode();
        CacheUtil.putCaptcha(pid,code.get("code"));
        return Map.of("base64",code.get("base64"),"pid",pid);
    }

    @Override
    public void generateEmailCode(String email,String pid,String code) {
        if(!Objects.equals(CacheUtil.getCaptchaAndRemove(pid), code)){
            throw new IllegalArgumentException("验证码错误");
        }

        String emailCode = RandomUtil.generateRandomString(6);
        CacheUtil.putEmail(email,emailCode);
        asyncService.sendCodeToEmail(email, emailCode);
    }

}
