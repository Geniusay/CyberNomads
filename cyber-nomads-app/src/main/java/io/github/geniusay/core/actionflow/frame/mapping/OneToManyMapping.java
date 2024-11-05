package io.github.geniusay.core.actionflow.frame.mapping;

import io.github.geniusay.core.actionflow.frame.ActionMapping;
import io.github.geniusay.core.actionflow.frame.Actor;
import io.github.geniusay.core.actionflow.frame.Receiver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述: 一对多
 * @author suifeng
 * 日期: 2024/11/5
 */
public class OneToManyMapping<A extends Actor, R extends Receiver> implements ActionMapping<A, R> {

    @Override
    public Map<A, List<R>> getMapping(List<A> actors, List<R> receivers) {
        Map<A, List<R>> mapping = new HashMap<>();
        if (actors.size() == 1) {
            mapping.put(actors.get(0), receivers);  // 一个执行者对多个接收者
        }
        return mapping;
    }
}