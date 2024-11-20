package io.github.geniusay.pojo.DO;

import com.baomidou.mybatisplus.annotation.*;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.pojo.DTO.ExportTaskTemplateDTO;
import io.github.geniusay.utils.ThreadUtil;
import lombok.Data;
import org.openqa.selenium.TakesScreenshot;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/20 10:35
 */
@Data
@TableName("task_template")
public class TaskTemplateDO {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String name;
    private String description;
    private String type;
    private String platform;
    private String params;
    private String uid;
    private Boolean hasPrivate;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    public static TaskTemplateDO convert(ExportTaskTemplateDTO task){
        TaskTemplateDO taskTemplateDO = new TaskTemplateDO();
        BeanUtils.copyProperties(task,taskTemplateDO);
        taskTemplateDO.uid = ThreadUtil.getUid();
        return taskTemplateDO;
    }
}
