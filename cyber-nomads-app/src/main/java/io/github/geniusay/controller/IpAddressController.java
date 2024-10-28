package io.github.geniusay.controller;

import io.github.common.web.Result;
import io.github.geniusay.proxy.ProxyManager;
import io.github.geniusay.utils.RequestUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

@RestController
public class IpAddressController {

    @Resource
    RequestUtil requestUtil;

    @GetMapping("/get-client-ip")
    public String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return Optional.ofNullable(ip).orElse("Unknown IP Address");
    }


    @GetMapping("/retry")
    public Result<?> retry() {
        return Result.success();
    }

    @GetMapping("/requestTest")
    public Result<?> requestTest() {
        System.out.println(requestUtil.get("http://119.3.234.15:9000/get-client-ip"));
        return Result.success();
    }
}