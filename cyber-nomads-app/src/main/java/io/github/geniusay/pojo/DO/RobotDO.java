package io.github.geniusay.pojo.DO;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/30 12:08
 */
@Data
@Builder
@TableName("robot")
@AllArgsConstructor
@NoArgsConstructor
public class RobotDO {

    private Long id;
    private Integer platform;
    private String nickname;
    private String username;
    private String cookie;
    private boolean ban;
    private boolean hasDelete;
    private String uid;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
