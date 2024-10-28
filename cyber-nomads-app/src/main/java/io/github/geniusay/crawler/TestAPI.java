package io.github.geniusay.crawler;

import io.github.geniusay.crawler.api.bilibili.BilibiliCommentApi;
import io.github.geniusay.crawler.api.bilibili.BilibiliVideoApi;
import io.github.geniusay.crawler.po.bilibili.CommentDetail;
import io.github.geniusay.crawler.po.bilibili.CommentPage;
import io.github.geniusay.crawler.po.bilibili.VideoDetail;
import org.junit.Test;

import java.util.List;

import static io.github.geniusay.crawler.BCookie.cookie;

public class TestAPI {

    @Test
    public void testGetVideoInfo() throws Exception {
        // 通过bvid获取视频详细信息
        VideoDetail videoDetailByBvid = BilibiliVideoApi.getVideoDetailById(cookie, "BV1TZ421E7Ci");

        System.out.println(videoDetailByBvid);

        // 输出视频标题和播放数
        if (videoDetailByBvid != null) {
            System.out.println("视频标题: " + videoDetailByBvid.getData().getTitle());
            System.out.println("播放数: " + videoDetailByBvid.getData().getStat().getView());
        } else {
            System.out.println("获取视频信息失败");
        }

        // 通过aid获取视频详细信息
        VideoDetail videoDetailByAid = BilibiliVideoApi.getVideoDetailById(cookie, "85440373");

        System.out.println(videoDetailByAid);

        // 输出视频标题和播放数
        if (videoDetailByAid != null) {
            System.out.println("视频标题: " + videoDetailByAid.getData().getTitle());
            System.out.println("播放数: " + videoDetailByAid.getData().getStat().getView());
        } else {
            System.out.println("获取视频信息失败");
        }
    }

    @Test
    public void testGetVideoComment() throws Exception {

        String oid = "1154116168";

        // 获取第一页的根评论，每页5条，按点赞数排序，不显示热评
        CommentPage commentPage = BilibiliCommentApi.getComments(cookie, oid, 1, 5, 0, 1);

        // 输出分页信息
        if (commentPage != null) {
            System.out.println("当前页码: " + commentPage.getPageNum());
            System.out.println("每页项数: " + commentPage.getPageSize());
            System.out.println("总评论数: " + commentPage.getTotalCount());
            System.out.println("总页数: " + commentPage.getTotalPages());

            // 输出根评论信息
            List<CommentDetail> comments = commentPage.getComments();
            for (CommentDetail comment : comments) {
                System.out.println("根评论者: " + comment.getUsername());
                System.out.println("根评论内容: " + comment.getMessage());
                System.out.println("点赞数: " + comment.getLike());
                System.out.println("回复数: " + comment.getRcount());
                System.out.println("评论者等级: " + comment.getLevel());
                System.out.println("评论时间: " + comment.getDate());
                System.out.println("根评论rpid: " + comment.getRpid());
                System.out.println("------------------------");

                // 获取该根评论下的回复
                CommentPage replyPage = BilibiliCommentApi.getReplies(cookie, oid, comment.getRpid(), 1, 5);

                // 输出回复信息
                if (replyPage != null) {
                    List<CommentDetail> replies = replyPage.getComments();
                    for (CommentDetail reply : replies) {
                        System.out.println("回复者: " + reply.getUsername());
                        System.out.println("回复内容: " + reply.getMessage());
                        System.out.println("点赞数: " + reply.getLike());
                        System.out.println("回复数: " + reply.getRcount());
                        System.out.println("评论者等级: " + reply.getLevel());
                        System.out.println("评论时间: " + reply.getDate());
                        System.out.println("------------------------");
                    }
                }
            }
        } else {
            System.out.println("获取评论失败");
        }
    }

    @Test
    public void sendComment() throws Exception {
        String oid = "1154116168";

        // 发送一级评论
        boolean isSuccess = BilibiliCommentApi.sendCommentOrReply(cookie, oid, "Genius 80我", null, null);

//        // 回复某条评论
//        String root = "245458827841";  // 根评论的rpid
//        String parent = "245458827841";  // 父评论的rpid
//        boolean isReplySuccess = BilibiliCommentApi.sendCommentOrReply(cookie, oid, "welsir666", root, parent);
    }

    @Test
    public void likeComment() throws Exception {
        String oid = "1154116168";
        String parent = "245458827841";
        boolean isReplySuccess = BilibiliCommentApi.likeComment(cookie, oid, parent);
    }

    @Test
    public void dislikeComment() throws Exception {
        String oid = "1154116168";
        String parent = "245458827841";
        boolean isReplySuccess = BilibiliCommentApi.dislikeComment(cookie, oid, parent);
    }

    @Test
    public void likeVideo() throws Exception {
        String oid = "1154116168";
        boolean isReplySuccess = BilibiliVideoApi.likeVideo(cookie, oid, 1);
    }

    @Test
    public void disLikeVideo() throws Exception {
        String bvid = "BV1TZ421E7Ci";
        boolean isReplySuccess = BilibiliVideoApi.likeVideo(cookie, bvid, 2);
    }
}
