package io.github.geniusay.controller;

import io.github.common.web.Result;
import io.github.geniusay.service.CommonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/common")
public class CommonController {

    @Resource
    CommonService commonService;

    @GetMapping("/platforms")
    public Result<?> getPlatforms(){
        return commonService.getPlatforms();
    }

}
