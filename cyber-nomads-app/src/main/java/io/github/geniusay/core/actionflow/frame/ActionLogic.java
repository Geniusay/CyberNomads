package io.github.geniusay.core.actionflow.frame;

/**
 * 描述: 执行逻辑
 * @author suifeng
 * 日期: 2024/11/5
 */
public abstract class ActionLogic<A extends Actor, R extends Receiver> {

    public abstract void performAction(A actor, R receiver) throws Exception;

    protected void logAction(A actor, R receiver, String actionDescription) {
        System.out.printf("%s 对 %s 执行了操作: %s%n", actor.getName(), receiver.getId(), actionDescription);
    }
}