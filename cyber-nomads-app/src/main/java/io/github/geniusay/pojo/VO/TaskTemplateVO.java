package io.github.geniusay.pojo.VO;

import io.github.geniusay.pojo.DO.TaskTemplateDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/20 10:39
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskTemplateVO {

    private String id;
    private String name;
    private String description;
    private String type;
    private String platform;
    private String params;
    private String uid;
    private Boolean hasPrivate;

    public static TaskTemplateVO convert(TaskTemplateDO templateDO){
        TaskTemplateVO templateVO = new TaskTemplateVO();
        BeanUtils.copyProperties(templateDO,templateVO);
        templateVO.id = String.valueOf(templateDO.getId());
        return templateVO;
    }
}
