package io.github.geniusay.controller;

import io.github.common.web.Result;
import io.github.geniusay.core.anno.Counters;
import io.github.geniusay.core.anno.TokenRequire;
import io.github.geniusay.pojo.DTO.AddRobotDTO;
import io.github.geniusay.pojo.DTO.ChangeRobotDTO;
import io.github.geniusay.pojo.DTO.GetCookieDTO;
import io.github.geniusay.pojo.DTO.UpdateCookieDTO;
import io.github.geniusay.service.RobotService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;

import static io.github.geniusay.constants.CountersConstant.TASK;

@RestController
@RequestMapping("/robot")
public class RobotController {

    @Resource
    private RobotService robotService;

    @PostMapping("/upload")
    @TokenRequire
    public Result<?> uploadRobot(@RequestParam("file") MultipartFile file){
        return Result.success(robotService.loadRobot(file));
    }

    @GetMapping("/search")
    @TokenRequire
    public Result<?> getUserRobot(){
        return Result.success(robotService.queryRobot());
    }

    @GetMapping("/delete")
    @TokenRequire
    public Result<?> removeRobot(@RequestParam("id") Long id){
        return Result.success(robotService.removeRoobot(id));
    }

    @GetMapping("/ban")
    @TokenRequire
    public Result<?> banRobot(@RequestParam("id") Long id){
        return Result.success(robotService.banRoobot(id));
    }

    @PostMapping("/change")
    @TokenRequire
    public Result<?> changeRobot(@RequestBody ChangeRobotDTO robotDTO){
        return Result.success(robotService.changeRobot(robotDTO));
    }

    @GetMapping("/platforms")
    public Result<?> getPlatforms(){
        return robotService.getPlatforms();
    }

    @PostMapping("/add")
    @TokenRequire
    public Result<?> addRobot(@RequestBody AddRobotDTO robotDTO){
        return Result.success(robotService.addRobot(robotDTO));
    }

    @PostMapping("/getCookie")
    @TokenRequire
    public Result<?> getCookie(@RequestBody @Valid GetCookieDTO getCookieDTO){
        return Result.success(robotService.getCookie(getCookieDTO));
    }

    @PostMapping("/changeCookie")
    @TokenRequire
    public Result<?> changeCookie(@RequestBody @Valid UpdateCookieDTO updateCookieDTO){
        return Result.success(robotService.changeRobotCookie(updateCookieDTO));
    }
}
