package io.github.geniusay.blueprint;

import io.github.geniusay.core.supertask.TerminatorFactory;
import io.github.geniusay.core.supertask.plugin.terminator.Terminator;
import io.github.geniusay.core.supertask.plugin.video.GetHotVideoPlugin;
import io.github.geniusay.core.supertask.task.*;
        import io.github.geniusay.core.supertask.taskblueprint.AbstractTaskBlueprint;
import io.github.geniusay.crawler.api.bilibili.BilibiliCommentApi;
import io.github.geniusay.crawler.po.bilibili.VideoDetail;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static io.github.geniusay.core.supertask.config.TaskPlatformConstant.BILIBILI;
import static io.github.geniusay.core.supertask.config.TaskTypeConstant.HOT_VIDEO_COMMENT;

@Component
public class BilibiliHotVideoCommentTaskBlueprint extends AbstractTaskBlueprint {

    @Resource
    GetHotVideoPlugin getHotVideoPlugin;

    @Override
    public String platform() {
        return BILIBILI;
    }

    @Override
    public String taskType() {
        return HOT_VIDEO_COMMENT;
    }

    @Override
    protected void executeTask(RobotWorker robot, Task task) throws Exception {
        Map<String, Object> params = task.getParams();

        String commentStr = (String) params.get("commentStr");
        String cookie = robot.getCookie();
        int videoCount = (int) params.get("videoCount");  // 获取用户指定的评论视频数量

        // 获取指定数量的热门视频
        List<VideoDetail> videoList = getHotVideoPlugin.getHandleVideoWithLimit(params, videoCount);

        // 遍历视频列表，逐个发送评论
        for (VideoDetail video : videoList) {
            String oid = String.valueOf(video.getData().getAid());  // 获取视频的评论区ID (aid)

            // 发送评论
            ApiResponse<Boolean> response = BilibiliCommentApi.sendCommentOrReply(cookie, oid, commentStr, null, null);

            if (!response.isSuccess()) {
                task.log("评论失败: 视频 %s (oid: %s), 错误信息: %s", video.getData().getBvid(), oid, response.getMsg());
                throw new RuntimeException("评论失败: " + response.getMsg());
            }

            // 使用终结器判断任务是否完成，并调用 doTask 方法
            if (terminator.doTask(robot)) {
                break;
            }
        }
    }

    @Override
    protected String lastWord(RobotWorker robot, Task task) {
        Terminator terminator = task.getTerminator();
        String robotName = robot.getNickname();
        String commentStr = (String) task.getParams().get("commentStr");

        if (terminator.taskIsDone()) {
            return String.format("[Success] %s robot 已经对所有指定视频发表评论，评论内容是: %s", robotName, commentStr);
        } else {
            return String.format("[Error] %s robot 执行任务失败，未能对所有视频发表评论", robotName);
        }
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return List.of(
                TerminatorFactory.getTerminatorParams(),
                new TaskNeedParams("hotVideoSearch", "热门视频指定参数", true, getHotVideoPlugin.supplierNeedParams()),
                new TaskNeedParams("commentStr", String.class, "评论的内容", true, "赛博游民"),
                new TaskNeedParams("videoCount", Integer.class, "需要评论的视频数量", true, 1, null)
        );
    }
}
