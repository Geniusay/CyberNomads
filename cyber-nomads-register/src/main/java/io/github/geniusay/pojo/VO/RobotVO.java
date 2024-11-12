package io.github.geniusay.pojo.VO;

import lombok.Builder;
import lombok.Data;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/30 12:17
 */
@Data
@Builder
public class RobotVO {

    private String id;
    private String platform;
    private Integer platformCode;
    private String platformCnZh;
    private boolean ban;
    private String nickname;
    private String username;
    private String createTime;

}