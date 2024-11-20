package io.github.geniusay.core.actionflow.frame;

import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 描述: 执行逻辑
 * @author suifeng
 * 日期: 2024/11/5
 */
@Slf4j
public abstract class ActionLogic<A extends Actor, R extends Receiver> {

    public abstract String getLogicName();

    public abstract ApiResponse<Boolean> performAction(A actor, R receiver) throws Exception;

    protected void logAction(A actor, R receiver, String actionDescription) {
        log.info("{} 对 {} 执行了操作: {}", actor.getName(), receiver.getId(), actionDescription);
    }
}