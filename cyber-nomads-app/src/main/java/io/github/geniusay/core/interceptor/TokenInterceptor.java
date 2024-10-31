package io.github.geniusay.core.interceptor;

import cn.dev33.satoken.stp.StpUtil;
import io.github.geniusay.core.anno.TokenRequire;
import io.github.geniusay.utils.CacheUtil;
import io.github.geniusay.utils.ThreadUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/28 14:55
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            if(handlerMethod.hasMethodAnnotation(TokenRequire.class)){
                String token = request.getHeader("Authorization");
                String uid = null;
                if (token!=null&& (uid = CacheUtil.tokenCache.get(token))!=null) {
                    String tokenObject = (String) StpUtil.getLoginIdByToken(token);
                    String[] strs = tokenObject.split("\\|");
                    if(StpUtil.isLogin(tokenObject)&&strs.length==2){
                        ThreadUtil.set("uid",strs[0]);
                        ThreadUtil.set("email",strs[1]);
                    }
                    return true;
                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                    return false;
                }
            }
        }
        return true;
    }

}
