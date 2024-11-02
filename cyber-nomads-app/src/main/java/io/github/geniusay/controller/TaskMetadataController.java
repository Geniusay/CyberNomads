package io.github.geniusay.controller;

import io.github.geniusay.core.exception.R;
import io.github.geniusay.service.TaskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RequestMapping("/task")
@RestController
public class TaskMetadataController {

    @Resource
    private TaskService taskService;

    /**
     * 获取支持的平台列表，返回包含英文和中文的平台列表
     * @return 平台列表
     */
    @GetMapping("/platforms")
    public R<?> listSupportedPlatforms() {
        return R.success(taskService.getSupportedPlatforms());
    }

    /**
     * 获取某个平台的所有操作功能及对应的参数列表
     * @param platform 平台名称
     * @return 功能及参数列表
     */
    @GetMapping("/platforms/{platform}/functions-params")
    public R<?> listFunctionsAndParamsByPlatform(@PathVariable String platform) {
        return R.success(taskService.getFunctionsAndParamsByPlatform(platform));
    }
}