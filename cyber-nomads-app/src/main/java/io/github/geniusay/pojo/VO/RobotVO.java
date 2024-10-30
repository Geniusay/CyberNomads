package io.github.geniusay.pojo.VO;

import io.github.geniusay.pojo.DO.RobotDO;
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

    private Long id;
    private Integer plat;
    private boolean ban;
    private String nickname;
    private String username;

    public static RobotVO convert(RobotDO r){
        return RobotVO.builder()
                .id(r.getId())
                .plat(r.getPlatform())
                .username(r.getUsername())
                .nickname(r.getNickname())
                .ban(r.isBan())
                .build();
    }
}
