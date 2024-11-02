package io.github.geniusay.pojo.DTO;

import lombok.Data;

import java.util.Map;

@Data
public class UpdateTaskDTO {

    private Long taskId;

    Map<String, Object> params;
}
