package io.github.geniusay.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/24 21:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkerTaskResponseDTO {

    private String workerId;
    private List<String> taskId;

}
