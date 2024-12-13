package io.github.geniusay.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddRobotDTO {

    private Integer platform;

    private String nickname;

    private String username;

    // 在qr模式下，代表验证的key
    private String cookie;
}
