package io.github.geniusay.service.Impl;

import io.github.geniusay.core.exception.ServeException;
import io.github.geniusay.mapper.RegisterMachineMapper;
import io.github.geniusay.pojo.DO.RegisterMachineDO;
import io.github.geniusay.service.LoginMachineService;
import io.github.geniusay.utils.*;
import org.apache.commons.lang.StringUtils;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

import static io.github.geniusay.constants.RedisConstant.LOGIN_MACHINE_CAPTCHA;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/11 14:57
 */
@Service
public class ILoginMachineServiceImpl implements LoginMachineService {

    @Resource
    RegisterMachineMapper mapper;
    @Resource
    StringEncryptor encryptor;

    @Resource
    ImageUtil imageUtil;
    @Resource
    CacheUtil cacheUtil;

    @Override
    public String generateCode() {
        String pid = ThreadUtil.getUid();
        if(cacheUtil.checkCaptchaExpired(LOGIN_MACHINE_CAPTCHA+pid)){
            throw new ServeException("请勿频繁请求,稍后再试");
        }
        Map<String, String> code = imageUtil.generateCode(6);
        String seeCode = CyberStringUtils.toUpper(code.get("code"));
        System.out.println(seeCode);
        String script = encryptor.encrypt(seeCode);
        String token = TokenUtil.getToken(ThreadUtil.getUid(), ThreadUtil.getEmail(), ThreadUtil.getNickname());
        cacheUtil.put(LOGIN_MACHINE_CAPTCHA+seeCode, token,600);
        cacheUtil.put(LOGIN_MACHINE_CAPTCHA+pid, token,600);
        return script;
    }

    @Override
    public Boolean logout(String scriptCode) {
        String captcha = encryptor.decrypt(scriptCode);
        cacheUtil.remove(LOGIN_MACHINE_CAPTCHA+captcha);
        return null;
    }

    @Override
    public Boolean verity() {
        return true;
    }

    @Override
    public RegisterMachineDO queryMachineInfo(int id) {
        return mapper.selectById(id);
    }
}
