package io.github.geniusay.pojo.VO;


import io.github.geniusay.pojo.DO.RobotDO;
import io.github.geniusay.pojo.DO.SharedRobotDO;
import io.github.geniusay.utils.TaskTranslationUtil;
import io.netty.util.internal.StringUtil;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class SharedRobotVO {
    private Long id;

    private Integer platform;

    private String nickname;

    private String username;

    private boolean ban;

    private boolean hasDelete;

    private String uid;

    private String createTime;

    private String updateTime;

    private List<String> taskTypes;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public SharedRobotVO(RobotDO robotDO, SharedRobotDO sharedRobotDO) {
        this.id = robotDO.getId();
        this.platform = robotDO.getPlatform();
        this.nickname = robotDO.getNickname();
        this.username = robotDO.getUsername();
        this.ban = robotDO.isBan();
        this.hasDelete = robotDO.isHasDelete();
        this.uid = robotDO.getUid();
        this.taskTypes = stringToList(sharedRobotDO.getFocusTask());
    }

    public SharedRobotVO(Long id, Integer platform, String nickname, String username, boolean ban, boolean hasDelete, String uid, Date createTime,Date updateTime, String focusTask) {
        this.id = id;
        this.platform = platform;
        this.nickname = nickname;
        this.username = username;
        this.ban = ban;
        this.hasDelete = hasDelete;
        this.uid = uid;
        this.createTime = sdf.format(createTime);
        this.updateTime = sdf.format(updateTime);
        this.taskTypes = stringToList(focusTask).stream().map(TaskTranslationUtil::translateTaskType).collect(Collectors.toList());
    }

    private static List<String> stringToList(String str) {
        if (StringUtil.isNullOrEmpty(str)) {
            return new ArrayList<>();
        }
        return Arrays.stream(str.split(",")).filter(s -> !"".equals(s)).collect(Collectors.toList());
    }
}