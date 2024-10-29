package io.github.geniusay.crawler.test.bilibili;

import io.github.geniusay.crawler.api.bilibili.BilibiliCommentApi;
import org.junit.Test;

import static io.github.geniusay.crawler.BCookie.cookie;

public class TestCommontAPI {

    @Test
    public void sendComment() throws Exception {
        String oid = "1154116168";
        // 发送一级评论
        boolean isSuccess = BilibiliCommentApi.sendCommentOrReply(cookie, oid, "Genius 80我", null, null);
    }

    @Test
    public void replyComment() throws Exception {
        String oid = "1154116168";
        // 回复某条评论
        String root = "245458827841";  // 根评论的rpid
        String parent = "245458827841";  // 父评论的rpid
        boolean isReplySuccess = BilibiliCommentApi.sendCommentOrReply(cookie, oid, "welsir666", root, parent);
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
}
