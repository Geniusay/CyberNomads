package io.github.geniusay.crawler.api.bilibili;

import io.github.geniusay.crawler.handler.bilibili.BilibiliCommentHandler;
import io.github.geniusay.crawler.po.bilibili.CommentPage;


/**
 * 描述: B站评论相关API
 * @author suifeng
 * 日期: 2024/10/28
 */
public class BilibiliCommentApi {

    /**
     * 获取视频根评论列表
     *
     * @param cookie 用户的Cookie
     * @param oid 视频的oid（目标评论区ID）
     * @param page 页码
     * @param size 每页项数
     * @param sort 排序方式（0：按时间，1：按点赞数，2：按回复数）
     * @param nohot 是否显示热评（0：显示，1：不显示）
     * @return CommentPage 评论分页信息和列表
     */
    public static CommentPage getComments(String cookie, String oid, int page, int size, int sort, int nohot) {
        return BilibiliCommentHandler.getComments(cookie, oid, page, size, sort, nohot);
    }

    /**
     * 获取指定根评论下的回复列表
     *
     * @param cookie 用户的Cookie
     * @param oid 视频的oid（目标评论区ID）
     * @param root 根评论的rpid
     * @param page 页码
     * @param size 每页项数
     * @return CommentPage 回复分页信息和列表
     */
    public static CommentPage getReplies(String cookie, String oid, String root, int page, int size) {
        return BilibiliCommentHandler.getReplies(cookie, oid, root, page, size);
    }

    /**
     * 发送评论或回复
     *
     * @param cookie 用户的Cookie
     * @param oid 视频的oid（目标评论区ID）
     * @param message 发送的评论内容
     * @param root 根评论的rpid（如果是回复某条评论则传递，否则为null）
     * @param parent 父评论的rpid（如果是二级或多级回复则传递，否则为null）
     * @return boolean 评论或回复是否发送成功
     */
    public static boolean sendCommentOrReply(String cookie, String oid, String message, String root, String parent) {
        return BilibiliCommentHandler.sendCommentOrReply(cookie, oid, message, root, parent);
    }
}