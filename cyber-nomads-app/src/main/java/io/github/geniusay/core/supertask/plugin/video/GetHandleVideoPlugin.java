package io.github.geniusay.core.supertask.plugin.video;

import io.github.geniusay.core.supertask.plugin.TaskPlugin;
import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.crawler.po.bilibili.BilibiliVideoDetail;

public interface GetHandleVideoPlugin extends TaskPlugin {

    BilibiliVideoDetail getHandleVideo(RobotWorker robot, Task task);
}
