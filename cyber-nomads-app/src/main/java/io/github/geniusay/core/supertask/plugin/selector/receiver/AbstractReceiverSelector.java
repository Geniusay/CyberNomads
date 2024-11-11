package io.github.geniusay.core.supertask.plugin.selector.receiver;

import io.github.geniusay.core.actionflow.frame.Receiver;
import io.github.geniusay.core.supertask.plugin.BaseTaskPlugin;

import static io.github.geniusay.core.supertask.config.PluginConstant.RECEIVER_SELECTOR_GROUP_NAME;

public abstract class AbstractReceiverSelector extends BaseTaskPlugin implements ReceiverSelector {

    @Override
    public String getPluginGroup() {
        return RECEIVER_SELECTOR_GROUP_NAME;
    }
}