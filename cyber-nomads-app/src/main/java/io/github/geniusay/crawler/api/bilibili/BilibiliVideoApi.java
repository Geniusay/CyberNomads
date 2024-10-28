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
}
