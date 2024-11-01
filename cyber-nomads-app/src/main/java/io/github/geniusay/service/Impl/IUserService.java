package io.github.geniusay.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.common.web.Result;
import io.github.geniusay.core.async.AsyncService;
import io.github.geniusay.mapper.RobotMapper;
import io.github.geniusay.mapper.UserMapper;
import io.github.geniusay.pojo.DO.RobotDO;
import io.github.geniusay.pojo.DO.UserDO;
import io.github.geniusay.pojo.DTO.*;
import io.github.geniusay.pojo.Platform;
import io.github.geniusay.pojo.VO.LoginVO;
import io.github.geniusay.pojo.VO.RobotVO;
import io.github.geniusay.pojo.VO.UserVO;
import io.github.geniusay.service.UserService;
import io.github.geniusay.utils.*;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/27 21:39
 */
//TODO 所有Assert全部变成抛出异常
@Service
public class IUserService implements UserService {

    @Resource
    UserMapper userMapper;
    @Resource
    RobotMapper robotMapper;
    @Resource
    ImageUtil imageUtil;
    @Resource
    private AsyncService asyncService;

    //TODO UserVO中缺少了nickname，添加一下这个字段
    @Override
    public UserVO queryUserById(String uid) {
        UserDO dto = userMapper.selectOne(new QueryWrapper<UserDO>().eq("uid",uid));
        Assert.notNull(dto,"用户不存在");
        return UserVO.convert(dto);
    }

    @Override
    public Result<UserVO> getUserInfo() {
        String uid = ThreadUtil.getUid();
        UserDO dto = userMapper.selectOne(new QueryWrapper<UserDO>().eq("uid",uid));
        return Result.success(UserVO.convert(dto));
    }

    //TODO CacheUtil 换成 Redis缓存，采用原子性
    @Override
    public LoginVO login(LoginRequestDTO req) {
        String code = req.getCode();
        String emailCode = CacheUtil.getEmailAndRemove(req.getEmail());
        if(!StringUtils.equals(CyberStringUtils.toLower(code), emailCode)){
            throw new IllegalArgumentException("验证码错误");
        }

        UserDO user = userMapper.selectOne(new QueryWrapper<UserDO>()
                .eq("email",req.getEmail()));
        Assert.notNull(user,"账号错误或用户不存在");
        String token = TokenUtil.getToken(String.valueOf(user.getId()), user.getEmail());
        CacheUtil.tokenCache.put(token,user.getUid());
        return LoginVO.builder().userVO(UserVO.convert(user)).token(token).build();
    }

    //TODO nickname自动生成，格式可以是 赛博游民+随机数
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

    //TODO 采用Redis缓存
    @Override
    public Map<String, String> generateCaptcha() {
        String pid = UUID.randomUUID().toString();
        Map<String, String> code = imageUtil.generateCode();
        CacheUtil.putCaptcha(pid, CyberStringUtils.toLower(code.get("code")));
        return Map.of("base64",code.get("base64"),"pid",pid);
    }

    //TODO 采用Redis缓存获取邮箱验证码，冷却时间1分钟
    @Override
    public void generateEmailCode(String email,String pid,String code) {
        if(!StringUtils.equals(CacheUtil.getCaptchaAndRemove(pid), CyberStringUtils.toLower(code))){
            throw new IllegalArgumentException("验证码错误");
        }
        if(CacheUtil.isExpired(email)){
            throw new RuntimeException("验证码冷却中");
        }
        String emailCode = RandomUtil.generateRandomString(6);
        CacheUtil.putEmail(email, CyberStringUtils.toLower(emailCode));
        asyncService.sendCodeToEmail(email, emailCode);
    }

    @Override
    public LoadRobotResponseDTO loadRobot(MultipartFile file) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> success = new ArrayList<>();
        List<String> fail = new ArrayList<>();
        try {
            List<UserCookieDTO> userDataList = objectMapper.readValue(file.getBytes(), new TypeReference<>() {
            });
            for (UserCookieDTO userCookieDTO : userDataList) {
                String cookieString = userCookieDTO.getCookie().stream()
                        .map(uCookie -> uCookie.getName() + "=" + uCookie.getValue())
                        .collect(Collectors.joining(";"));
                RobotDO build = RobotDO.builder()
                        .username(userCookieDTO.getUsername())
                        .nickname(userCookieDTO.getUsername())
                        .platform(Platform.BILIBILI.getCode())
                        .cookie(cookieString)
                        .ban(false)
                        .hasDelete(false)
                        .build();
                robotMapper.insert(build);
                userMapper.insertUserRobot(ThreadUtil.getUid(),build.getId());
                success.add(build.getUsername());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return LoadRobotResponseDTO.builder().successRobot(success).errorRobot(fail).build();
    }


    //TODO 下面所有的robot接口，包括controller，另起一个RobotController和RobotService
    @Override
    public List<RobotVO> queryRobot() {
        String uid = ThreadUtil.getUid();
        List<RobotDO> robotDOs = userMapper.queryRobotsByUid(uid);
        return robotDOs.stream().map(RobotVO::convert).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Boolean removeRoobot(Long id) {
        String uid = ThreadUtil.getUid();
        return userMapper.delRobot(uid,id)==1&&robotMapper.update(null,new UpdateWrapper<RobotDO>().eq("id",id).set("has_delete",true))==1;
    }

    @Override
    public Boolean banRoobot(Long id) {
        return robotMapper.update(null,new UpdateWrapper<RobotDO>().eq("id",id).set("ban",true))==1;
    }

}
