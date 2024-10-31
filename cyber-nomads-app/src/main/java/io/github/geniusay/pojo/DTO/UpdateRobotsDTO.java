package io.github.geniusay.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRobotsDTO {
    private Long taskId;
    private List<Long> robotIds;
    private boolean isAdd;
}