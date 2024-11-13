package io.github.geniusay.core.supertask.plugin.selector.logic;

import io.github.geniusay.core.actionflow.frame.ActionLogic;
import io.github.geniusay.core.actionflow.logic.BiliCommentLogic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import static io.github.geniusay.core.supertask.config.PluginConstant.COMMENT_LOGIC_SELECTOR;

@Slf4j
@Scope("prototype")
@Component(COMMENT_LOGIC_SELECTOR)
public class BiliCommentLogicSelector extends AbstractLogicSelector implements LogicSelector {

    @Override
    public ActionLogic getLogic(String help) {
        return new BiliCommentLogic(help);
    }
}