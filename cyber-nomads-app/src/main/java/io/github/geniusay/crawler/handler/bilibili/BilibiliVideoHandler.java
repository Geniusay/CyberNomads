package io.github.geniusay.crawler.handler.bilibili;

import io.github.geniusay.crawler.po.bilibili.VideoDetail;
import io.github.geniusay.utils.HttpClientUtil;

import java.io.IOException;

/**
 * 描述: B站视频相关爬虫处理器
 * @author suifeng
 * 日期: 2024/10/28
 */
public class BilibiliVideoHandler {

    // 获取视频详细信息的URL
    public static final String VIDEO_DETAIL_BY_BVID = "https://api.bilibili.com/x/web-interface/view?bvid=";
    public static final String VIDEO_DETAIL_BY_AID = "https://api.bilibili.com/x/web-interface/view?aid=";

    /**
     * 通过bvid或aid获取视频详细信息
     *
     * @param cookie 用户的Cookie
     * @param id 视频的bvid或aid
     * @return VideoDetail 对象
     */
    public static VideoDetail getVideoDetailById(String cookie, String id) {
        String url;

        // 判断id是否以"BV"开头
        if (id.startsWith("BV")) {
            url = VIDEO_DETAIL_BY_BVID + id;
        } else {
            url = VIDEO_DETAIL_BY_AID + id;
        }

        try {
            String response = HttpClientUtil.sendGetRequest(url, cookie);
            return HttpClientUtil.parseJson(response, VideoDetail.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
