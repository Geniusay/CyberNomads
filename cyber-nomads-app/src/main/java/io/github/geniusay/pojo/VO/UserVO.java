package io.github.geniusay.pojo.VO;

import io.github.geniusay.pojo.DTO.UserDTO;
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
    private Long uid;
    private String avator;
    private String email;
    private String phoneNum;
    private Integer point;


    public static UserVO convert(UserDTO userDTO){
        UserVO build = UserVO.builder().id(userDTO.getId()).avator(userDTO.getAvator()).email(userDTO.getEmail()).point(userDTO.getPoint()).phoneNum(userDTO.getPhoneNum()).build();
        return build;

    }
}
