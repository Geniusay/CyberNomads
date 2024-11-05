package io.github.geniusay.core.actionflow.frame.mapping;

import io.github.geniusay.core.actionflow.frame.ActionMapping;
import io.github.geniusay.core.actionflow.frame.Actor;
import io.github.geniusay.core.actionflow.frame.Receiver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述: 多对多
 * @author suifeng
 * 日期: 2024/11/5
 */
public class ManyToManyMapping<A extends Actor, R extends Receiver> implements ActionMapping<A, R> {

    @Override
    public Map<A, List<R>> getMapping(List<A> actors, List<R> receivers) {
        Map<A, List<R>> mapping = new HashMap<>();
        for (A actor : actors) {
            mapping.put(actor, receivers);  // 每个执行者对所有接收者执行操作
        }
        return mapping;
    }
}