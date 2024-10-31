package io.github.geniusay.pojo.DO;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/29 19:36
 */
@Builder
@Data
@TableName("register_machine")
public class RegisterMachineDO {

    private Integer id;
    private String url;
    private String code;
    private String version;
}
