package io.github.geniusay.pojo.VO;

import io.github.geniusay.pojo.DO.RobotDO;
import io.github.geniusay.pojo.Platform;
import io.github.geniusay.utils.PlatformUtil;
import io.github.geniusay.utils.TaskTranslationUtil;
import io.github.geniusay.utils.TimeUtil;
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
    private String plat;
    private boolean ban;
    private String nickname;
    private String username;
    private String createTime;

    public static RobotVO convert(RobotDO r){
        return RobotVO.builder()
                .id(String.valueOf(r.getId()))
                .plat(TaskTranslationUtil.translatePlatform(PlatformUtil.getPlatformByCode(r.getPlatform())))
                .username(r.getUsername())
                .nickname(r.getNickname())
                .ban(r.isBan())
                .createTime(TimeUtil.getFormatTimeStr(r.getCreateTime()))
                .build();
    }
}
