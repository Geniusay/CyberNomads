package io.github.geniusay.core.interceptor;

import cn.dev33.satoken.stp.StpUtil;
import io.github.geniusay.core.anno.LoginMachineToken;
import io.github.geniusay.core.anno.TokenRequire;
import io.github.geniusay.utils.CacheUtil;
import io.github.geniusay.utils.ThreadUtil;
import org.apache.commons.lang.StringUtils;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static io.github.geniusay.constants.RedisConstant.LOGIN_MACHINE_CAPTCHA;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/16 1:30
 */
@Component
public class LoginMachineTokenInterceptor implements HandlerInterceptor {
    @Resource
    CacheUtil cacheUtil;
    @Resource
    StringEncryptor encryptor;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String machineToken;
        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            if(handlerMethod.hasMethodAnnotation(LoginMachineToken.class)){
                machineToken = request.getHeader("machine-token");
                if(!StringUtils.isBlank(machineToken)){
                    machineToken = machineToken.replace(" ","");
                    String script = encryptor.decrypt(machineToken);
                    machineToken = cacheUtil.get(LOGIN_MACHINE_CAPTCHA+script);
                    if(!StringUtils.isBlank(machineToken)){
                        request.setAttribute("Authorization",machineToken);
                    }
                }
            }
        }
        return true;
    }
}
