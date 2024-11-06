package io.github.geniusay.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@Builder
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class ChangeRobotDTO {
    @NotNull
    private Long id;

    private Integer platform;

    @NotNull
    private String nickname;

    @NotNull
    private String username;

}
