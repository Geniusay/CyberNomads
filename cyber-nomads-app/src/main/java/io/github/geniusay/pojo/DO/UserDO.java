package io.github.geniusay.pojo.DO;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/27 21:50
 */
@Data
@Builder
@TableName("user")
public class UserDO {

    private Long id;
    private String uid;
    private String avator;
    private String email;
    private String password;
    private String nickname;
    private String phoneNum;
    private Integer point;

}
