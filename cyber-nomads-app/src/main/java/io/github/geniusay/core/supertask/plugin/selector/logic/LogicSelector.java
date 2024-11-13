package io.github.geniusay.core.supertask.plugin.selector.logic;

import io.github.geniusay.core.actionflow.frame.ActionLogic;
import io.github.geniusay.core.supertask.plugin.TaskPlugin;

/**
 * 逻辑行为选择器插件
 */
public interface LogicSelector extends TaskPlugin {

    ActionLogic getLogic();

    default ActionLogic getLogic(String help) {
        return getLogic();
    }
}
