package io.github.geniusay.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.common.web.Result;
import io.github.geniusay.constants.UserConstant;
import io.github.geniusay.mapper.UserMapper;
import io.github.geniusay.pojo.DO.UserDO;
import io.github.geniusay.pojo.DTO.RegisterRequestDTO;
import io.github.geniusay.pojo.VO.LoginVO;
import io.github.geniusay.pojo.VO.UserVO;
import io.github.geniusay.utils.CacheUtil;
import io.github.geniusay.utils.RandomUtil;
import io.github.geniusay.utils.TokenUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * 开放帮助使用的test接口
 */
@RequestMapping("/test")
@RestController
public class TestController {

    @Resource
    private UserMapper userMapper;

    @Resource
    private CacheUtil cacheUtil;

    @PostMapping("/register")
    public Result<?> registerUser(@RequestBody RegisterRequestDTO req){
        UserDO user = UserDO.builder()
                .nickname(UserConstant.CYBER_NOMADS + RandomUtil.generateRandomString(8))
                .uid(UUID.randomUUID().toString())
                .avatar("https://geniusserve.oss-cn-shanghai.aliyuncs.com/cybernomads/plant.ico")
                .email(req.getEmail()).point(3)
                .password(DigestUtils.md5Hex(req.getPassword()))
                .build();
        userMapper.insert(user);
        String token = TokenUtil.getToken(user.getUid(), user.getEmail(), user.getNickname());
        cacheUtil.putTokenAndUid(token,user.getUid());
        return Result.success(LoginVO.builder().userVO(UserVO.convert(user)).token(token).build());
    }

    @GetMapping("/login")
    public Result<?> loginUser(@RequestParam String email){
        UserDO user = userMapper.selectOne(new QueryWrapper<UserDO>()
                .eq("email", email));
        if(user == null){
            throw new RuntimeException("用户未注册");
        }
        String token = TokenUtil.getToken(user.getUid(), user.getEmail(), user.getNickname());
        cacheUtil.putTokenAndUid(token,user.getUid());
        return Result.success(LoginVO.builder().userVO(UserVO.convert(user)).token(token).build());
    }
}
