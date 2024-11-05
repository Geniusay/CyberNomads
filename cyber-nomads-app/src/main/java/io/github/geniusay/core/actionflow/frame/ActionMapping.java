package io.github.geniusay.core.actionflow.frame;

import java.util.List;
import java.util.Map;

public interface ActionMapping<A extends Actor, R extends Receiver> {
    Map<A, List<R>> getMapping(List<A> actors, List<R> receivers);
}