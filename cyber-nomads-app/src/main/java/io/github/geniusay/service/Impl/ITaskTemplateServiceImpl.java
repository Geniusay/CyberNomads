package io.github.geniusay.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.geniusay.constants.RedisConstant;
import io.github.geniusay.core.exception.ServeException;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.mapper.TaskMapper;
import io.github.geniusay.mapper.TaskTemplateMapper;
import io.github.geniusay.pojo.DO.TaskDO;
import io.github.geniusay.pojo.DO.TaskTemplateDO;
import io.github.geniusay.pojo.DTO.ExportTaskTemplateDTO;
import io.github.geniusay.pojo.DTO.QueryTemplateDTO;
import io.github.geniusay.pojo.VO.TaskTemplateVO;
import io.github.geniusay.service.TaskTemplateService;
import io.github.geniusay.utils.CacheUtil;
import io.github.geniusay.utils.ThreadUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/20 10:41
 */
@Component
public class ITaskTemplateServiceImpl implements TaskTemplateService {
    final int DEFAULT_LIMIT = 10;
    final int DEFAULT_PAGE = 1;
    @Resource
    TaskTemplateMapper templateMapper;
    @Resource
    TaskMapper taskMapper;
    @Resource
    CacheUtil cacheUtil;
    @Override
    public void insertTemplate(ExportTaskTemplateDTO task) {
        String uid = ThreadUtil.getUid();
        TaskDO taskDO = taskMapper.selectOne(new QueryWrapper<TaskDO>()
                .eq("uid", uid)
                .eq("id", task.getTaskId()));
        if(taskDO==null){
            throw new ServeException("找不到该任务");
        }
        TaskTemplateDO templateDO = TaskTemplateDO.convert(task);
        templateDO.setParams(taskDO.getParams());
        templateDO.setPlatform(taskDO.getPlatform());
        templateDO.setType(taskDO.getTaskType());
        templateMapper.insert(templateDO);
    }

    @Override
    public List<TaskTemplateVO> queryTemplateByPage(QueryTemplateDTO queryTemplateDTO) {
        QueryWrapper<TaskTemplateDO> wrapper = new QueryWrapper<>();
        Optional.ofNullable(queryTemplateDTO.getName()).ifPresent(name -> wrapper.like("name", name));
        Optional.ofNullable(queryTemplateDTO.getType()).ifPresent(type -> wrapper.eq("type", type));
        if (Boolean.TRUE.equals(queryTemplateDTO.getQuerySelf())) {
            wrapper.eq("uid", ThreadUtil.getUid());
        }
        int limit = Optional.ofNullable(queryTemplateDTO.getLimit()).orElse(DEFAULT_LIMIT);
        int page = Optional.ofNullable(queryTemplateDTO.getPage()).orElse(DEFAULT_PAGE);
        Page<TaskTemplateDO> selectedPage = templateMapper.selectPage(new Page<>(page-1, limit), wrapper);
        return selectedPage.getRecords().stream().map(TaskTemplateVO::convert).collect(Collectors.toList());
    }

    @Override
    public String generateTemplateScript(String templateId) {
        Integer integer = templateMapper.selectCount(new QueryWrapper<TaskTemplateDO>().eq("id", templateId));
        if(integer!=1){
            throw new ServeException("查询不到对应模板");
        }
        String uuid = UUID.randomUUID().toString();
        cacheUtil.put(RedisConstant.TASK_TEMPLATE_KEY+uuid,templateId);
        cacheUtil.put(RedisConstant.TASK_TEMPLATE_KEY+templateId,uuid);
        return uuid;
    }

    @Override
    public TaskTemplateVO importTemplateScript(String script) {
        String templateId = cacheUtil.get(RedisConstant.TASK_TEMPLATE_KEY + script);
        TaskTemplateDO templateDO = templateMapper.selectOne(new QueryWrapper<TaskTemplateDO>().eq("id", templateId));
        return TaskTemplateVO.convert(templateDO);
    }

    @Override
    public void removeTemplate(String templateId) {
        removeTemplateCache(templateId);
        templateMapper.deleteById(templateId);
    }

    private void removeTemplateCache(String templateId){
        String uuid = cacheUtil.get(RedisConstant.TASK_TEMPLATE_KEY + templateId);
        cacheUtil.remove(RedisConstant.TASK_TEMPLATE_KEY+uuid);
        cacheUtil.remove(RedisConstant.TASK_TEMPLATE_KEY+templateId);
    }
}
