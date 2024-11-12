package io.github.geniusay.core.interceptor;

import cn.dev33.satoken.stp.StpUtil;
import io.github.geniusay.core.anno.LoginMachineToken;
import io.github.geniusay.core.anno.TokenRequire;
import io.github.geniusay.utils.CacheUtil;
import io.github.geniusay.utils.ThreadUtil;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Arrays;

import static io.github.geniusay.constants.RedisConstant.LOGIN_MACHINE_CAPTCHA;

@Component
public class LoginMachineTokenInterceptor implements HandlerInterceptor, Ordered {

    @Resource
    CacheUtil cacheUtil;
    @Resource
    StringEncryptor encryptor;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            if(handlerMethod.hasMethodAnnotation(LoginMachineToken.class)){
                String token = request.getHeader("machine-token");
                System.out.println(token);
                System.out.println("获取到machine-token:"+token);
                String origin = encryptor.decrypt(token);
                String machineToken = cacheUtil.get(LOGIN_MACHINE_CAPTCHA + origin);
                System.out.println(machineToken);
                if(machineToken==null)
                    return false;
                request.setAttribute("Authorization",machineToken);
                return true;
            }
        }
        return true;
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
