package io.github.geniusay.core.event.commonEvent;

import io.github.geniusay.core.event.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/13 17:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RobotSelecteEvent extends Event {

    private Long robotId;

}
