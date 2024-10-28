package io.github.geniusay.interceptor;

import cn.dev33.satoken.stp.StpUtil;
import io.github.geniusay.utils.CacheUtil;
import io.github.geniusay.utils.TokenUtil;
import org.springframework.stereotype.Component;
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
        String token = request.getHeader("Authorization");
        if (token!=null&&CacheUtil.tokenCache.get(token)!=null) {
            String tokenObject = (String) StpUtil.getLoginIdByToken(token);
            if(tokenObject!=null){

            }
            return true;
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            return false;
        }
    }

}
