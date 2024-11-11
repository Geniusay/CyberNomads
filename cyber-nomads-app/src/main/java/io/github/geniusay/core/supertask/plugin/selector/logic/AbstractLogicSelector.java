package io.github.geniusay.core.supertask.plugin.selector.logic;

import io.github.geniusay.core.supertask.plugin.BaseTaskPlugin;

import static io.github.geniusay.core.supertask.config.PluginConstant.LOGIC_SELECTOR_GROUP_NAME;

public abstract class AbstractLogicSelector extends BaseTaskPlugin implements LogicSelector {

    @Override
    public String getPluginGroup(){
        return LOGIC_SELECTOR_GROUP_NAME;
    }
}
