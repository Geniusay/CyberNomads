package io.github.geniusay.core.counters.strategy;

import io.github.geniusay.core.counters.AbstractCounters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CountersStrategy {
    private final Map<String, AbstractCounters> counters;

    @Autowired
    public CountersStrategy(ApplicationContext applicationContext) {
        counters = applicationContext.getBeansOfType(AbstractCounters.class);
    }

    public AbstractCounters getCounter(String countersType) {
        return counters.get(countersType);
    }
}
