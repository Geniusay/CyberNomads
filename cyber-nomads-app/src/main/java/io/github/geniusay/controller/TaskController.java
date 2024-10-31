package io.github.geniusay.controller;

import io.github.geniusay.pojo.DO.TaskDO;
import io.github.geniusay.pojo.DTO.UpdateRobotsDTO;
import io.github.geniusay.service.ITaskService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Resource
    private ITaskService taskService;

    @PostMapping("/create")
    public String createTask(
            @RequestParam String taskName,
            @RequestParam String platform,
            @RequestParam String taskType,
            @RequestBody Map<String, Object> params) {
        // 创建任务
        taskService.createTask(taskName, platform, taskType, params);
        return "任务创建成功";
    }

    /**
     * 根据用户 uid 获取所有任务
     */
    @GetMapping("/user/{uid}")
    public List<TaskDO> getUserTasks(@PathVariable String uid) {
        return taskService.getUserTasks(uid);
    }

    /**
     * 批量添加或删除机器人账号
     */
    @PostMapping("/robots/update")
    public String updateRobotsInTask(@RequestBody UpdateRobotsDTO updateRobotsDTO) {
        taskService.updateRobotsInTask(updateRobotsDTO.getTaskId(), updateRobotsDTO.getRobotIds(), updateRobotsDTO.isAdd());
        return updateRobotsDTO.isAdd() ? "机器人账号添加成功" : "机器人账号删除成功";
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