package io.github.geniusay.crawler.test.bilibili;

import io.github.geniusay.crawler.api.bilibili.BilibiliCommentApi;
import io.github.geniusay.crawler.po.bilibili.CommentDetail;
import io.github.geniusay.crawler.po.bilibili.CommentPage;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import org.junit.Test;

import java.util.List;

import static io.github.geniusay.crawler.BCookie.cookie;

public class TestCommentAPI {

    @Test
    public void sendComment() throws Exception {
        String oid = "113525699904858";
        // 发送一级评论
        ApiResponse<Boolean> booleanApiResponse = BilibiliCommentApi.sendCommentOrReply(cookie, oid, "请问而不可撒娇去玩儿全物品", null, null);
        System.out.println(booleanApiResponse);
    }

    @Test
    public void getVideoComment() throws Exception {
        String oid = "592055874";
        // 获取第一页的根评论，每页5条，按点赞数排序，不显示热评
        ApiResponse<CommentPage> response = BilibiliCommentApi.getComments(cookie, oid, 1, 20, 1, 1);
        if (response.isSuccess()) {
            CommentPage commentPage = response.getData();
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
//                    // 获取该根评论下的回复
//                    ApiResponse<CommentPage> response1 = BilibiliCommentApi.getReplies(cookie, oid, comment.getRpid(), 1, 5);
//
//                    if (response1.isSuccess()) {
//                        CommentPage replyPage = response1.getData();
//                        // 输出回复信息
//                        if (replyPage != null) {
//                            List<CommentDetail> replies = replyPage.getComments();
//                            for (CommentDetail reply : replies) {
//                                System.out.println("回复者: " + reply.getUsername());
//                                System.out.println("回复内容: " + reply.getMessage());
//                                System.out.println("点赞数: " + reply.getLike());
//                                System.out.println("回复数: " + reply.getRcount());
//                                System.out.println("评论者等级: " + reply.getLevel());
//                                System.out.println("评论时间: " + reply.getDate());
//                                System.out.println("------------------------");
//                            }
//                        }
//                    }
                }
            } else {
                System.out.println("获取评论失败");
            }
        }
    }

    @Test
    public void replyComment() throws Exception {
        String oid = "1154116168";
        // 回复某条评论
        String root = "245458827841";  // 根评论的rpid
        String parent = "245458827841";  // 父评论的rpid
        BilibiliCommentApi.sendCommentOrReply(cookie, oid, "welsir666", root, parent);
    }

    @Test
    public void likeComment() throws Exception {
        String oid = "1154116168";
        String parent = "245458827841";
        BilibiliCommentApi.likeComment(cookie, oid, parent);
    }

    @Test
    public void dislikeComment() throws Exception {
        String oid = "1154116168";
        String parent = "245458827841";
        BilibiliCommentApi.dislikeComment(cookie, oid, parent);
    }
}
