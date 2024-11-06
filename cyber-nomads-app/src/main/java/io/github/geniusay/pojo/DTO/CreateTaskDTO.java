package io.github.geniusay.pojo.DTO;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CreateTaskDTO {

    private String taskName;

    private String platform;

    private String taskType;

    Map<String, Object> params;

    private List<Long> robotIds;
}
