package io.github.geniusay.core.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class TestInterceptor implements HandlerInterceptor {

    @Value("${spring.profiles.active}")
    private String env;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 允许访问
        if(env.equals("dev")){
            log.info("{} execute",request.getPathInfo());
            return true;
        }else{
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "unknown request");
            return false;
        }
    }
}
