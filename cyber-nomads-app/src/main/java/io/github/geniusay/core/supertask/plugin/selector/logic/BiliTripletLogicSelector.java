package io.github.geniusay.core.supertask.plugin.selector.logic;

import io.github.geniusay.core.actionflow.frame.ActionLogic;
import io.github.geniusay.core.actionflow.logic.BiliTripletLogic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static io.github.geniusay.core.supertask.config.PluginConstant.TRIPLET_LOGIC_SELECTOR;

@Slf4j
@Scope("prototype")
@Component(TRIPLET_LOGIC_SELECTOR)
public class BiliTripletLogicSelector extends AbstractLogicSelector implements LogicSelector {

    @Override
    public ActionLogic getLogic() {
        return new BiliTripletLogic();
    }
}