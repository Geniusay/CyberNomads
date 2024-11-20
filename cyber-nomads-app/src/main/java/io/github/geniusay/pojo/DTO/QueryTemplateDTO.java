package io.github.geniusay.pojo.DTO;

import lombok.Data;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/20 11:51
 */
@Data
public class QueryTemplateDTO {

    private String type;
    private String name;
    private Integer limit;
    private Integer page;
    private Boolean querySelf;
}
