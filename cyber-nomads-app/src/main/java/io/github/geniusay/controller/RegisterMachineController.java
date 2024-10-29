package io.github.geniusay.controller;

import io.github.common.web.Result;
import io.github.geniusay.service.RegisterMachineService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/29 17:04
 */
@RestController
@RequestMapping("/machine")
public class RegisterMachineController {
    @Resource
    RegisterMachineService service;
    @GetMapping("/search/{id}")
    public Result<?> getRegisterMachine(@PathVariable Integer id){
        return Result.success(service.queryMachineInfo(id));
    }

}
