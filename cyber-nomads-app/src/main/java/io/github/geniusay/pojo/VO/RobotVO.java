package io.github.geniusay.pojo.VO;

import io.github.geniusay.pojo.DO.RobotDO;
import io.github.geniusay.pojo.Platform;
import io.github.geniusay.utils.PlatformUtil;
import io.github.geniusay.utils.TaskTranslationUtil;
import io.github.geniusay.utils.TimeUtil;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;

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
    private boolean hasCookie;

    public static RobotVO convert(RobotDO r){
        return RobotVO.builder()
                .id(String.valueOf(r.getId()))
                .platform(PlatformUtil.getPlatformByCode(r.getPlatform()))
                .platformCode(r.getPlatform())
                .platformCnZh(TaskTranslationUtil.translatePlatform(PlatformUtil.getPlatformByCode(r.getPlatform())))
                .username(r.getUsername())
                .nickname(r.getNickname())
                .ban(r.isBan())
                .hasCookie(Objects.nonNull(r.getCookie()))
                .createTime(TimeUtil.getFormatTimeStr(r.getCreateTime()))
                .build();
    }
}
