package io.github.geniusay.core.event;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/13 17:39
 */
@Component
public class EventManager {

    @Resource
    ApplicationContext applicationContext;
    private List<EventListener> listeners = new ArrayList<>();

    @PostConstruct
    public void init(){
        Map<String, EventListener> beansOfType = applicationContext.getBeansOfType(EventListener.class);
        listeners = new ArrayList<>(beansOfType.values());
    }

    public void publishEvent(Event event) {
        for (EventListener listener : listeners) {
            listener.pushEvent(event);
        }
    }
}
