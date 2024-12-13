package io.github.geniusay.core.event.commonEvent;

import io.github.geniusay.core.event.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/21 23:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoveWorkerIfNeedEvent extends Event {

    private String workerId;

}
