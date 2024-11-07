package io.github.geniusay.controller;

import io.github.geniusay.core.exception.R;
import io.github.geniusay.service.TaskService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RequestMapping("/task")
@RestController
public class TaskMetadataController {

    @Resource
    private TaskService taskService;

    /**
     * 获取某个平台的所有操作功能及对应的参数列表
     * @param platform 平台名称
     * @return 功能及参数列表
     */
    @GetMapping("/platforms/functions-params")
    public R<?> listFunctionsAndParamsByPlatform(@RequestParam String platform) {
        return R.success(taskService.getFunctionsAndParamsByPlatform(platform));
    }
}
