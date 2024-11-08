package io.github.geniusay.blueprint;

import io.github.geniusay.core.supertask.plugin.TaskPluginFactory;
import io.github.geniusay.core.supertask.plugin.terminator.GroupCountTerminator;
import io.github.geniusay.core.supertask.plugin.terminator.Terminator;
import io.github.geniusay.core.supertask.task.*;
import io.github.geniusay.core.supertask.taskblueprint.AbstractTaskBlueprint;
import io.github.geniusay.crawler.api.bilibili.BilibiliCommentApi;
import io.github.geniusay.utils.ParamsUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.github.geniusay.constants.TerminatorConstants.TERMINATOR_TYPE_GROUP_COUNT;
import static io.github.geniusay.core.supertask.config.TaskPlatformConstant.BILIBILI;
import static io.github.geniusay.core.supertask.config.TaskTypeConstant.VIDEO_COMMENT;

@Component
public class BilibiliVideoCommentTaskBlueprint extends AbstractTaskBlueprint {

    @Resource
    TaskPluginFactory taskPluginFactory;

    private static final String BVID = "bvid";

    private static final String OID = "oid";

    private static final String TEXT = "text";

    @Override
    public String platform() {
        return BILIBILI;
    }

    @Override
    public String taskType() {
        return VIDEO_COMMENT;
    }

    @Override
    protected void executeTask(RobotWorker robot, Task task) throws Exception {
        Map<String, Object> userParams = task.getParams();
        String oid = getValue(userParams, OID, String.class);
        String text = getValue(userParams,TEXT, String.class);
        BilibiliCommentApi.sendCommentOrReply(robot.getCookie(), oid, text, null, null);
    }

    @Override
    protected String lastWord(RobotWorker robot, Task task) {
        Terminator terminator = task.getTerminator();
        String robotName = robot.getNickname();
        String commentStr = (String) task.getParams().get(TEXT);

        if (terminator.taskIsDone()) {
            return String.format("[Success] %s robot 已经对所指定视频发表评论，评论内容是: %s", robotName, commentStr);
        } else {
            return String.format("[Error] %s robot 执行任务失败，未能对所有视频发表评论", robotName);
        }
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        List<TaskNeedParams> bluePrintParams = List.of(
                TaskNeedParams.ofKV(BVID,"","视频的BV号"),
                TaskNeedParams.ofK(OID,String.class,"评论区id(也就是视频的aid, 不传则通过BV去获取)"),
                TaskNeedParams.ofKV(TEXT,"I Am Cyber Nomads!","评论内容")
        );
        List<TaskNeedParams> pluginParams = taskPluginFactory.pluginGroupParams(GroupCountTerminator.class);
        return ParamsUtil.packageListParams(bluePrintParams, pluginParams);
    }
}
