package io.github.geniusay.service.Impl;

import io.github.geniusay.core.exception.ServeException;
import io.github.geniusay.pojo.DTO.VerityCodeDTO;
import io.github.geniusay.service.LoginMachineService;
import io.github.geniusay.utils.CacheUtil;
import io.github.geniusay.utils.CyberStringUtils;
import io.github.geniusay.utils.ImageUtil;
import io.github.geniusay.utils.ThreadUtil;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.UUID;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/11 14:57
 */
@Service
public class ILoginMachineServiceImpl implements LoginMachineService {

    @Resource
    StringRedisTemplate template;
    @Resource
    ImageUtil imageUtil;
    @Resource
    CacheUtil cacheUtil;

    @Override
    public String verity(VerityCodeDTO verityCodeDTO) {
        String uid = verityCodeDTO.getUid();
        String code = verityCodeDTO.getCode();
        if(!StringUtils.equals(cacheUtil.getCaptchaAndRemove(uid), CyberStringUtils.toUpper(code))){
            throw new ServeException("验证码错误");
        }


        return null;
    }

    @Override
    public String generateCode() {
        String pid = ThreadUtil.getUid();
        if(cacheUtil.captchaCodeIsExpired(pid)){
            throw new ServeException("请勿频繁请求,稍后再试");
        }
        Map<String, String> code = imageUtil.generateCode(6);
        String seeCode = CyberStringUtils.toUpper(code.get("code"));
        cacheUtil.putCaptcha(pid, seeCode);
        return code.get("base64");
    }
}
