package io.github.geniusay.pojo.VO;

import io.github.geniusay.pojo.DO.UserDO;
import lombok.Builder;
import lombok.Data;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/27 21:52
 */
@Data
@Builder
public class UserVO {

    private Long id;
    private String uid;
    private String avator;
    private String email;
    private String phoneNum;
    private Integer point;


    public static UserVO convert(UserDO userDO){
        return UserVO.builder().id(userDO.getId()).uid(userDO.getUid()).avator(userDO.getAvator()).email(userDO.getEmail()).point(userDO.getPoint()).phoneNum(userDO.getPhoneNum()).build();

    }
}