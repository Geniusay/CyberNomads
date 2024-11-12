package io.github.geniusay.core.supertask.plugin.selector.logic;

import io.github.geniusay.core.actionflow.frame.ActionLogic;
import io.github.geniusay.core.actionflow.logic.BiliCoinLogic;
import io.github.geniusay.core.actionflow.logic.BiliFollowLogic;
import io.github.geniusay.core.actionflow.logic.BiliLikeLogic;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static io.github.geniusay.constants.PluginConstant.*;
import static io.github.geniusay.core.supertask.config.PluginConstant.FOLLOW_LOGIC_SELECTOR;
import static io.github.geniusay.core.supertask.config.PluginConstant.LIKE_LOGIC_SELECTOR;

@Slf4j
@Scope("prototype")
@Component(FOLLOW_LOGIC_SELECTOR)
public class BiliFollowLogicSelector extends AbstractLogicSelector implements LogicSelector {

    private Integer reSrc;

    @Override
    public void init(Task task) {
        super.init(task);
        reSrc = getValue(this.pluginParams, RE_SRC, Integer.class);
    }

    @Override
    public ActionLogic getLogic() {
        return new BiliFollowLogic(reSrc);
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return List.of(
                TaskNeedParams.ofKV(RE_SRC, 11, "关注来源代码 (如：空间11, 视频14，文章115，活动页面222)")
        );
    }
}