package io.github.geniusay.pojo.DO;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/27 21:50
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class UserDO {

    public UserDO(Integer point) {
        this.point = point;
    }
    private Long id;
    private String uid;
    private String avatar;
    private String email;
    private String password;
    private String nickname;
    private String phoneNum;
    private Integer point;

}
