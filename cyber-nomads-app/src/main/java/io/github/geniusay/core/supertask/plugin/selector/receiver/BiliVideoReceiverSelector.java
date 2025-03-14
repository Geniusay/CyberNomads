package io.github.geniusay.core.supertask.plugin.selector.receiver;

import io.github.geniusay.core.actionflow.frame.Receiver;
import io.github.geniusay.core.actionflow.receiver.BiliVideoReceiver;
import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static io.github.geniusay.constants.PluginConstant.*;
import static io.github.geniusay.core.supertask.config.PluginConstant.VIDEO_RECEIVER_SELECTOR;
import static io.github.geniusay.utils.BilibiliFormatUtil.extractBvId;

@Slf4j
@Scope("prototype")
@Component(VIDEO_RECEIVER_SELECTOR)
public class BiliVideoReceiverSelector extends AbstractReceiverSelector implements ReceiverSelector {

    private String bvid;

    @Override
    public void init(Task task) {
        super.init(task);
        String linkOrBV = getValue(this.pluginParams, LINK_OR_BV, String.class);
        bvid = extractBvId(linkOrBV);
    }

    @Override
    public Receiver getReceiver() {
        return new BiliVideoReceiver(bvid);
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return List.of(
                TaskNeedParams.ofKV(LINK_OR_BV, "", "视频的链接/BV号")
        );
    }
}
