package io.github.geniusay.core.supertask.plugin.selector.receiver;

import io.github.geniusay.core.actionflow.frame.Receiver;
import io.github.geniusay.core.actionflow.receiver.BiliUserReceiver;
import io.github.geniusay.core.actionflow.receiver.BiliVideoReceiver;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static io.github.geniusay.constants.PluginConstant.*;
import static io.github.geniusay.core.supertask.config.PluginConstant.USER_RECEIVER_SELECTOR;
import static io.github.geniusay.core.supertask.config.PluginConstant.VIDEO_RECEIVER_SELECTOR;

@Slf4j
@Scope("prototype")
@Component(USER_RECEIVER_SELECTOR)
public class BiliUserReceiverSelector extends AbstractReceiverSelector implements ReceiverSelector {

    private String uid;

    @Override
    public void init(Task task) {
        super.init(task);
        uid = getValue(this.pluginParams, UID, String.class);
    }

    @Override
    public Receiver getReceiver() {
        return new BiliUserReceiver(uid);
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return List.of(
                TaskNeedParams.ofKV(UID, "", "用户的uid").setExtendDesc(UID_EXT_DESC)
        );
    }
}
