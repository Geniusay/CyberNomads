package io.github.geniusay.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskVO {

    private Long id;
    private String uid;
    private String nickname;
    private String taskName;
    private String platform;
    private String taskType;
    private String taskStatus;
    private List<Long> robots;
    private Map<String, Object> params;
    private String createTime;

    private String platformCnZh;
    private String taskTypeCnZh;
}

