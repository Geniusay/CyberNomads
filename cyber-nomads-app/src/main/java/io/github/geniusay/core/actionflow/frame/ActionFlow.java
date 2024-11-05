package io.github.geniusay.core.actionflow.frame;

import java.util.List;

import java.util.Map;

public class ActionFlow<A extends Actor, R extends Receiver> {

    private final List<A> actors;
    private final ActionLogic<A, R> actionLogic;
    private final List<R> receivers;
    private final ActionMapping<A, R> actionMapping;

    public ActionFlow(List<A> actors, ActionLogic<A, R> actionLogic, List<R> receivers, ActionMapping<A, R> actionMapping) {
        this.actors = actors;
        this.actionLogic = actionLogic;
        this.receivers = receivers;
        this.actionMapping = actionMapping;
    }

    public ActionFlow(A actor, ActionLogic<A, R> actionLogic, List<R> receivers, ActionMapping<A, R> actionMapping) {
        this(List.of(actor), actionLogic, receivers, actionMapping);
    }

    public ActionFlow(List<A> actors, ActionLogic<A, R> actionLogic, R receiver, ActionMapping<A, R> actionMapping) {
        this(actors, actionLogic, List.of(receiver), actionMapping);
    }

    public ActionFlow(A actor, ActionLogic<A, R> actionLogic, R receiver) {
        this(List.of(actor), actionLogic, List.of(receiver), null);
    }

    public void execute() throws Exception {
        if (actionMapping == null) {
            // 一对一的情况，直接执行
            actionLogic.performAction(actors.get(0), receivers.get(0));
        } else {
            // 多对多的情况，使用映射
            Map<A, List<R>> mapping = actionMapping.getMapping(actors, receivers);
            for (Map.Entry<A, List<R>> entry : mapping.entrySet()) {
                A actor = entry.getKey();
                List<R> receiversForActor = entry.getValue();
                for (R receiver : receiversForActor) {
                    actionLogic.performAction(actor, receiver);
                }
            }
        }
    }
}