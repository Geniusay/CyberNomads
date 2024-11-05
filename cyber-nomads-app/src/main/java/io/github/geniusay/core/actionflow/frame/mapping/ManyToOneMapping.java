package io.github.geniusay.core.actionflow.frame.mapping;

import io.github.geniusay.core.actionflow.frame.ActionMapping;
import io.github.geniusay.core.actionflow.frame.Actor;
import io.github.geniusay.core.actionflow.frame.Receiver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述: 多对一
 * @author suifeng
 * 日期: 2024/11/5
 */
public class ManyToOneMapping<A extends Actor, R extends Receiver> implements ActionMapping<A, R> {

    @Override
    public Map<A, List<R>> getMapping(List<A> actors, List<R> receivers) {
        Map<A, List<R>> mapping = new HashMap<>();
        if (receivers.size() == 1) {
            // 多个执行者对一个接收者
            for (A actor : actors) {
                mapping.put(actor, receivers);
            }
        }
        return mapping;
    }
}