package io.github.geniusay.pojo.DTO;

import lombok.Data;

import java.util.Map;

@Data
public class CreatTaskDTO {

    private String taskName;

    private String platform;

    private String taskType;

    Map<String, Object> params;
}
