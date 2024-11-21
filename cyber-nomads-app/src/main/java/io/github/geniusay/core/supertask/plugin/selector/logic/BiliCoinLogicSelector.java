package io.github.geniusay.core.supertask.plugin.selector.logic;

import io.github.geniusay.core.actionflow.frame.ActionLogic;
import io.github.geniusay.core.actionflow.logic.BiliCoinLogic;
import io.github.geniusay.core.actionflow.logic.BiliLikeLogic;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static io.github.geniusay.constants.PluginConstant.*;
import static io.github.geniusay.core.supertask.config.PluginConstant.COIN_LOGIC_SELECTOR;
import static io.github.geniusay.core.supertask.config.PluginConstant.LIKE_LOGIC_SELECTOR;

@Slf4j
@Scope("prototype")
@Component(COIN_LOGIC_SELECTOR)
public class BiliCoinLogicSelector extends AbstractLogicSelector implements LogicSelector {

    private Integer coinSum;

    private boolean andLike = false;

    @Override
    public void init(Task task) {
        super.init(task);
        int sum = getValue(this.pluginParams, COIN_SUM, Integer.class);
        if (sum == 1 || sum == 2) {
            coinSum = sum;
        } else {
            coinSum = 1;
        }
//        this.andLike = getValue(this.pluginParams, AND_LIKE, Boolean.class);
    }

    @Override
    public ActionLogic getLogic() {
        return new BiliCoinLogic(coinSum, andLike, false);
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return List.of(
                TaskNeedParams.ofKV(COIN_SUM, 1, "投币数量，填(1或2)")
//                TaskNeedParams.ofKV(AND_LIKE, false, "投币+点赞").setHidden(true).setExtendDesc(AND_LIKE_EXT_DESC)
        );
    }
}
