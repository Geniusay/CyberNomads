package io.github.geniusay.controller;

import io.github.common.web.Result;
import io.github.geniusay.pojo.DTO.CreatTaskDTO;
import io.github.geniusay.pojo.DTO.UpdateRobotsDTO;
import io.github.geniusay.service.TaskService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Resource
    private TaskService taskService;

    @PostMapping("/create")
    public Result<?> createTask(@RequestBody CreatTaskDTO creat) {
        taskService.createTask(creat.getTaskName(), creat.getPlatform(), creat.getTaskType(), creat.getParams());
        return Result.success("任务创建成功");
    }

    /**
     * 根据用户 uid 获取所有任务
     */
    @GetMapping("/user/{uid}")
    public Result<?> getUserTasks(@PathVariable String uid) {
        return Result.success(taskService.getUserTasks(uid));
    }

    /**
     * 批量添加或删除机器人账号
     */
    @PostMapping("/robots/update")
    public String updateRobotsInTask(@RequestBody UpdateRobotsDTO updateRobotsDTO) {
        taskService.updateRobotsInTask(updateRobotsDTO.getTaskId(), updateRobotsDTO.getRobotIds(), updateRobotsDTO.isHasAdd());
        return updateRobotsDTO.isHasAdd() ? "机器人账号添加成功" : "机器人账号删除成功";
    }

    /**
     * 修改任务的 params 参数
     */
    @PostMapping("/{taskId}/params/update")
    public String updateTaskParams(@PathVariable Long taskId, @RequestBody Map<String, Object> params) {
        taskService.updateTaskParams(taskId, params);
        return "任务参数更新成功";
    }
}