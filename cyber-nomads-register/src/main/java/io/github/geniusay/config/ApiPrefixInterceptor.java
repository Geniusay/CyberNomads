package io.github.geniusay.config;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/15 21:08
 */
public class ApiPrefixInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/api")) {
            // 去除请求前缀 "/api"
            String newRequestURI = requestURI.replace("/api","");
            request.getRequestDispatcher(newRequestURI).forward(request, response);
            return false; // 不继续处理原始请求
        }
        return true; // 继续处理原始请求
    }
    // 其他方法省略...
}

