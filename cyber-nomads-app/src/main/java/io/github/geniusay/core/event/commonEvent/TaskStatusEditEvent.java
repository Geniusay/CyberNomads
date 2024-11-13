package io.github.geniusay.core.event.commonEvent;

import io.github.geniusay.core.event.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/13 18:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatusEditEvent extends Event {

    private String taskId;
    private String status;

}
