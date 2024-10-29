package io.github.geniusay.pojo.DO;

import lombok.Builder;
import lombok.Data;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/29 19:36
 */
@Builder
@Data
public class RegisterMachineDO {

    private Integer id;
    private String url;
    private String code;
    private String version;

}
