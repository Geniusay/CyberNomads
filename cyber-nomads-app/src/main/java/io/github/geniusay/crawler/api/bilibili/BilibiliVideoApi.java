package io.github.geniusay.crawler.api.bilibili;

import io.github.geniusay.crawler.handler.bilibili.BilibiliVideoHandler;
import io.github.geniusay.crawler.po.bilibili.VideoDetail;

/**
 * 描述: B站视频相关API
 * @author suifeng
 * 日期: 2024/10/28
 */
public class BilibiliVideoApi {

    /**
     * 通过bvid或aid获取视频详细信息
     *
     * @param cookie 用户的Cookie
     * @param id 视频的bvid或aid
     * @return VideoDetail 对象
     */
    public static VideoDetail getVideoDetailById(String cookie, String id) {
        return BilibiliVideoHandler.getVideoDetailById(cookie, id);
    }

    /**
     * 点赞或取消点赞视频
     *
     * @param cookie 用户的Cookie
     * @param id 视频的bvid或aid
     * @param like 1表示点赞，2表示取消赞
     * @return boolean 点赞或取消点赞是否成功
     */
    public static boolean likeVideo(String cookie, String id, int like) {
        return BilibiliVideoHandler.likeVideo(cookie, id, like);
    }

    /**
     * 为视频投币
     *
     * @param cookie 用户的Cookie
     * @param id 视频的bvid或aid
     * @param multiply 投币数量，最大为2
     * @param selectLike 是否同时点赞，0表示不点赞，1表示同时点赞
     * @return boolean 投币是否成功
     */
    public static boolean coinVideo(String cookie, String id, int multiply, int selectLike) {
        return BilibiliVideoHandler.coinVideo(cookie, id, multiply, selectLike);
    }

    /**
     * 收藏或取消收藏视频
     *
     * @param cookie 用户的Cookie
     * @param rid 视频的aid
     * @param addMediaIds 要加入的收藏夹ID（可选）
     * @param delMediaIds 要取消的收藏夹ID（可选）
     * @return boolean 收藏或取消收藏是否成功
     */
    public static boolean favVideo(String cookie, String rid, String addMediaIds, String delMediaIds) {
        return BilibiliVideoHandler.favVideo(cookie, rid, addMediaIds, delMediaIds);
    }

    /**
     * 一键三连（点赞、投币、收藏）
     *
     * @param cookie 用户的Cookie
     * @param id 视频的bvid或aid
     * @return boolean 一键三连是否成功
     */
    public static boolean tripleAction(String cookie, String id) {
        return BilibiliVideoHandler.tripleAction(cookie, id);
    }
}
