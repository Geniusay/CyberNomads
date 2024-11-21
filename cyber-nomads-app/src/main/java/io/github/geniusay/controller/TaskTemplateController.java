package io.github.geniusay.controller;

import io.github.common.web.Result;
import io.github.geniusay.core.anno.TokenRequire;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.pojo.DTO.ExportTaskTemplateDTO;
import io.github.geniusay.pojo.DTO.QueryTemplateDTO;
import io.github.geniusay.service.TaskTemplateService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/19 22:38
 */
@RestController
@RequestMapping("/task/template")
@Validated
public class TaskTemplateController {

    @Resource
    TaskTemplateService templateService;

    @PostMapping("/export")
    @TokenRequire
    public Result<?> exportTaskTemplate(@RequestBody @Validated
                                   ExportTaskTemplateDTO task){
        templateService.insertTemplate(task);
        return Result.success();
    }

    @TokenRequire
    @PostMapping("/query")
    public Result<?> queryUserTemplateByPage(@RequestBody @Validated QueryTemplateDTO queryTemplateDTO){
        return Result.success(templateService.queryTemplateByPage(queryTemplateDTO));
    }

    @TokenRequire
    @GetMapping("/share")
    public Result<?> shareTaskTemplate(String templateId){
        return Result.success(templateService.generateTemplateScript(templateId));
    }

    @TokenRequire
    @GetMapping("/import")
    public Result<?> importTaskTemplate(String script){
        return Result.success(templateService.importTemplateScript(script));
    }

    @TokenRequire
    @GetMapping("/delete")
    public Result<?> deleteTemplate(String templateId){
        templateService.removeTemplate(templateId);
        return Result.success();
    }
}
