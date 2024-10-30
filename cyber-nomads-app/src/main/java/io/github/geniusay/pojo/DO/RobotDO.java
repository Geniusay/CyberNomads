package io.github.geniusay.pojo.DO;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/30 12:08
 */
@Data
@Builder
@TableName("robot")
public class RobotDO {

    private Long id;
    private Integer platform;
    private String nickname;
    private String username;
    private String cookie;
    private String password;
    private boolean ban;
    private boolean hasDelete;
}
