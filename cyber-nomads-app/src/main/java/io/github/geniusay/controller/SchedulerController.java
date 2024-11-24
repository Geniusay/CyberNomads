package io.github.geniusay.controller;

import io.github.common.web.Result;
import io.github.geniusay.schedule.TaskScheduleManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Description 调度器控制面板，获取当前robot已经对应worker
 * @Author welsir
 * @Date 2024/11/24 20:28
 */
@RequestMapping("/control/scheduler")
@RestController
public class SchedulerController {

    @Resource
    TaskScheduleManager manager;
    @GetMapping("/queryWorker")
    public Result<?> getAllRobot(){
        return Result.success(manager.getAllRobot());
    }

    @GetMapping("/queryTask")
    public Result<?> getAllTask(){
        return Result.success(manager.getWorldTask());
    }
}
