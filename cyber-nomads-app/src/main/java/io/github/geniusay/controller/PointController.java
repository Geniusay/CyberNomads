package io.github.geniusay.controller;

import io.github.common.web.Result;
import io.github.geniusay.utils.UserPointUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin")
public class PointController {
    @Resource
    private UserPointUtil userPointUtil;

    @Value("${point.reset.key}")
    private String resetKey;

    @PostMapping("/resetUserPoint")
    public Result<?> resetUserPoint(@RequestParam String uid, @RequestParam String key){
        if(resetKey.equals(key)){
            userPointUtil.resetUserPoint(uid);
            return Result.success();
        }
        return Result.error("406", "密钥错误");
    }
}
