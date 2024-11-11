package io.github.geniusay.core.supertask.plugin.selector.receiver;

import io.github.geniusay.core.actionflow.frame.Receiver;
import io.github.geniusay.core.supertask.plugin.TaskPlugin;
import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.Task;

/**
 * 接受者选择器插件
 */
public interface ReceiverSelector extends TaskPlugin {

    Receiver getReceiver();
}