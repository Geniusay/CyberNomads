package io.github.geniusay.core.supertask.plugin.selector.logic;

import io.github.geniusay.core.actionflow.frame.ActionLogic;
import io.github.geniusay.core.actionflow.logic.BiliFollowLogic;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static io.github.geniusay.constants.PluginConstant.*;
import static io.github.geniusay.core.supertask.config.PluginConstant.FOLLOW_LOGIC_SELECTOR;

@Slf4j
@Scope("prototype")
@Component(FOLLOW_LOGIC_SELECTOR)
public class BiliFollowLogicSelector extends AbstractLogicSelector implements LogicSelector {

    private Integer reSrc;

    // 关注来源的映射表，用户友好展示
    private static final Map<String, Integer> RE_SRC_MAPPING = Map.of(
            "空间", 11,
            "视频", 14,
            "文章", 115,
            "活动页面", 222
    );

    @Override
    public void init(Task task) {
        super.init(task);
        String selectedSource = getValue(this.pluginParams, RE_SRC, String.class);
        reSrc = RE_SRC_MAPPING.getOrDefault(selectedSource, 11);
    }

    @Override
    public ActionLogic getLogic() {
        return new BiliFollowLogic(reSrc);
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return List.of(
                TaskNeedParams.ofSelection(RE_SRC, "空间", "关注来源", List.of(
                        TaskNeedParams.ofK("空间", String.class, "空间"),
                        TaskNeedParams.ofK("视频", String.class, "视频"),
                        TaskNeedParams.ofK("文章", String.class, "文章"),
                        TaskNeedParams.ofK("活动页面", String.class, "活动页面")
                ), RE_SRC_EXT_DESC, true)
        );
    }
}
