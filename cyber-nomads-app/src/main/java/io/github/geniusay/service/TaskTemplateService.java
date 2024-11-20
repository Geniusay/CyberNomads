package io.github.geniusay.service;

import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.pojo.DTO.ExportTaskTemplateDTO;
import io.github.geniusay.pojo.DTO.QueryTemplateDTO;
import io.github.geniusay.pojo.VO.TaskTemplateVO;

import java.util.List;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/20 10:34
 */
public interface TaskTemplateService {

    void insertTemplate(ExportTaskTemplateDTO task);
    List<TaskTemplateVO> queryTemplateByPage(QueryTemplateDTO queryTemplateDTO);
    String generateTemplateScript(String templateId);
    TaskTemplateVO importTemplateScript(String script);
    void removeTemplate(String templateId);
}
