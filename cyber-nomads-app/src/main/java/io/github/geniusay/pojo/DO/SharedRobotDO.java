package io.github.geniusay.pojo.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@TableName("shared_robot")
@NoArgsConstructor
@AllArgsConstructor
public class SharedRobotDO {
    @TableId(value = "robot_id")
    private Long robotId;

    private String focusTask;

    private String nickname;

    private Float qualificationRate;

    private Date sharedTime;
}
