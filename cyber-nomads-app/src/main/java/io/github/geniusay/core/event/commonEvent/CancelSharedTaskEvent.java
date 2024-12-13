package io.github.geniusay.core.event.commonEvent;

import io.github.geniusay.core.event.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.checkerframework.checker.units.qual.A;

@Data
@AllArgsConstructor
public class CancelSharedTaskEvent extends Event {
    private Long robotId;

    private String uid;
}
