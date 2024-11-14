package io.github.geniusay.core.supertask.plugin.reply;

import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import io.github.geniusay.crawler.api.bilibili.BilibiliCommentApi;
import io.github.geniusay.crawler.api.bilibili.BilibiliVideoApi;
import io.github.geniusay.crawler.po.bilibili.CommentDetail;
import io.github.geniusay.crawler.po.bilibili.CommentPage;
import io.github.geniusay.utils.BilibiliFormatUtil;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static io.github.geniusay.constants.PluginConstant.LINK_OR_BV;
import static io.github.geniusay.core.supertask.config.PluginConstant.HOT_COMMENT_PLUGIN;

@Slf4j
@Scope("prototype")
@Component(HOT_COMMENT_PLUGIN)
public class GetHotCommentPlugin extends AbstractGetCommentPlugin implements GetHandleCommentPlugin {

    private String bvid;
    private String oid;

    // 存储热评的集合
    private final List<CommentDetail> hotComments = new ArrayList<>();
    private final List<CommentDetail> backupComments = new ArrayList<>(); // 备份的评论，用于复用
    private int currentPage = 1; // 当前页码
    private final int pageSize = 20; // 每次获取 20 条评论

    @Override
    public void init(Task task) {
        super.init(task);
        try {
            String linkOrBV = getValue(this.pluginParams, LINK_OR_BV, String.class);
            bvid = BilibiliFormatUtil.extractBvId(linkOrBV);
            oid = String.valueOf(BilibiliVideoApi.getVideoDetailById(null, bvid).getData().getData().getAid());

            // 尝试获取热评
            fetchHotComments();
        } catch (Exception e) {
            log.error("初始化 GetHotCommentPlugin 失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 分5次获取热评，每次20条，总共最多获取100条
     * 如果某一页没有评论或评论数量不足，则提前终止后续请求
     */
    private void fetchHotComments() {
        try {
            for (int i = 0; i < 5; i++) {
                ApiResponse<CommentPage> response = BilibiliCommentApi.getComments(null, oid, currentPage, pageSize, 1, 0);

                if (response.isSuccess() && response.getData() != null) {
                    List<CommentDetail> comments = response.getData().getComments();

                    // 如果没有更多评论，则提前终止
                    if (comments.isEmpty()) {
//                        log.info("第 {} 页没有更多评论，提前终止获取", currentPage);
                        break;
                    }

                    hotComments.addAll(comments);
                    backupComments.addAll(comments);
//                    log.info("第 {} 页成功获取到 {} 条热评", currentPage, comments.size());
                    currentPage++;
                } else {
                    log.warn("获取第 {} 页热评失败，状态码: {}, 错误信息: {}", currentPage, response.getCode(), response.getMsg());
                    break;
                }
            }
        } catch (Exception e) {
            log.error("获取热评时数量过少");
        }
    }

    @Override
    public CommentDetail getHandleComment() {
        if (hotComments.isEmpty()) {
//            log.info("热评列表为空，重新复用之前获取的评论");
            hotComments.addAll(backupComments);
        }

        // 随机选择一个评论
        int index = new Random().nextInt(hotComments.size());
        CommentDetail selectedComment = hotComments.get(index);

        // 移除该评论
        hotComments.remove(index);

        selectedComment.setBvid(bvid);
        selectedComment.setOid(oid);

//        log.info("返回并移除评论: {}", selectedComment.getMessage());
        return selectedComment;
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return List.of(
                TaskNeedParams.ofKV(LINK_OR_BV, "", "视频的链接/BV号")
        );
    }
}