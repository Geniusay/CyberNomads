package io.github.geniusay.blueprint;

import io.github.geniusay.core.supertask.plugin.TaskPluginFactory;
import io.github.geniusay.core.supertask.plugin.terminator.GroupCountTerminator;
import io.github.geniusay.core.supertask.task.*;
import io.github.geniusay.core.supertask.taskblueprint.AbstractTaskBlueprint;
import io.github.geniusay.crawler.api.bilibili.BilibiliCommentApi;
import io.github.geniusay.crawler.api.bilibili.BilibiliVideoApi;
import io.github.geniusay.crawler.po.bilibili.VideoDetail;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import io.github.geniusay.pojo.DO.LastWord;
import io.github.geniusay.utils.LastWordUtil;
import io.github.geniusay.utils.ParamsUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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
    protected void executeTask(RobotWorker robot, Task task) {
        Map<String, Object> userParams = task.getParams();
        String bvid = getValue(userParams,BVID,String.class);
        ApiResponse<VideoDetail> video = BilibiliVideoApi.getVideoDetailById(robot.getCookie(), bvid);
        long aid = video.getData().getData().getAid();
        String text = getValue(userParams,TEXT, String.class);
        ApiResponse<Boolean> response = BilibiliCommentApi.sendCommentOrReply(robot.getCookie(), String.valueOf(aid), text, null, null);
        Map<String, Object> additionalInfo = Map.of("bvid", video.getData().getData().getBvid(), "comment",text);
        LastWord lastWord = new LastWord(response, additionalInfo);
        task.addLastWord(robot, lastWord);
    }

    @Override
    protected String lastWord(RobotWorker robot, Task task, LastWord lastWord) {
        ApiResponse<Boolean> response = lastWord.getResponse();
        String bvid = (String) lastWord.getAdditionalInfo("bvid");
        String comment = (String) lastWord.getAdditionalInfo("comment");

        if (response.isSuccess()) {
            return LastWordUtil.buildLastWord(String.format("%s robot 成功对视频 %s 发表评论，评论内容: %s", robot.getNickname(), bvid, comment), true);
        } else {
            return LastWordUtil.buildLastWord(String.format("%s robot 发表评论失败，视频: %s，状态码: %d，错误消息: %s", robot.getNickname(), bvid, response.getCode(), response.getMsg()), false);
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
