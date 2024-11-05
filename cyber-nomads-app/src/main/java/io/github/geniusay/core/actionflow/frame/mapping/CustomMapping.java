package io.github.geniusay.core.actionflow.frame.mapping;

import io.github.geniusay.core.actionflow.frame.ActionMapping;
import io.github.geniusay.core.actionflow.frame.Actor;
import io.github.geniusay.core.actionflow.frame.Receiver;

import java.util.List;
import java.util.Map;

/**
 * 描述: 自定义映射
 * @author suifeng
 * 日期: 2024/11/5
 */
public class CustomMapping<A extends Actor, R extends Receiver> implements ActionMapping<A, R> {

    private final Map<A, List<R>> customMapping;

    public CustomMapping(Map<A, List<R>> customMapping) {
        this.customMapping = customMapping;
    }

    @Override
    public Map<A, List<R>> getMapping(List<A> actors, List<R> receivers) {
        return customMapping;
    }
}