package io.github.geniusay.core.supertask.plugin.selector.receiver;

import io.github.geniusay.core.actionflow.frame.Receiver;
import io.github.geniusay.core.actionflow.receiver.BiliCommentReceiver;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import io.github.geniusay.crawler.api.bilibili.BilibiliVideoApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static io.github.geniusay.constants.PluginConstant.LINK_OR_BV;
import static io.github.geniusay.core.supertask.config.PluginConstant.COMMENT_RECEIVER_SELECTOR;
import static io.github.geniusay.utils.BilibiliFormatUtil.extractBvId;

@Slf4j
@Scope("prototype")
@Component(COMMENT_RECEIVER_SELECTOR)
public class BiliCommentReceiverSelector extends AbstractReceiverSelector implements ReceiverSelector {

    private String bvid;
    private String oid;

    @Override
    public void init(Task task) {
        super.init(task);
        String linkOrBV = getValue(this.pluginParams, LINK_OR_BV, String.class);
        bvid = extractBvId(linkOrBV);
        oid = String.valueOf(BilibiliVideoApi.getVideoDetailById(null, bvid).getData().getData().getAid());
    }

    @Override
    public Receiver getReceiver() {
        return new BiliCommentReceiver(bvid, oid);
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return List.of(
                TaskNeedParams.ofKV(LINK_OR_BV, "", "视频的链接/BV号")
        );
    }
}