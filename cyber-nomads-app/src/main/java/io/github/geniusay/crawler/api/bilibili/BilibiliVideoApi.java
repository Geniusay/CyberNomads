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
}
