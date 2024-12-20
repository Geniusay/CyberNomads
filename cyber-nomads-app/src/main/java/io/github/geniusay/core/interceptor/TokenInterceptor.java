package io.github.geniusay.core.interceptor;

import cn.dev33.satoken.stp.StpUtil;
import io.github.geniusay.core.anno.LoginMachineToken;
import io.github.geniusay.core.anno.TokenRequire;
import io.github.geniusay.utils.CacheUtil;
import io.github.geniusay.utils.ThreadUtil;
import org.apache.commons.lang.StringUtils;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Enumeration;

import static io.github.geniusay.constants.RedisConstant.LOGIN_MACHINE_CAPTCHA;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/28 14:55
 */
@Component
public class TokenInterceptor implements HandlerInterceptor, Ordered {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            if(handlerMethod.hasMethodAnnotation(TokenRequire.class)){
                String token = request.getHeader("Authorization");
                token = token==null? (String) request.getAttribute("Authorization") :token;
                String tokenObject = (String) StpUtil.getLoginIdByToken(token);
                if(tokenObject == null){
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                    return false;
                }
                String[] strs = tokenObject.split("\\|");
                ThreadUtil.set("uid",strs[0]);
                ThreadUtil.set("email",strs[1]);
                ThreadUtil.set("nickname",strs[2]);
                return true;
            }
        }
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ThreadUtil.remove();
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
