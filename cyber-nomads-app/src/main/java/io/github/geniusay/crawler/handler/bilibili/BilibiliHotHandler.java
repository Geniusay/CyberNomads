package io.github.geniusay.crawler.handler.bilibili;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.geniusay.crawler.po.bilibili.CommentDetail;
import io.github.geniusay.crawler.po.bilibili.CommentPage;
import io.github.geniusay.crawler.po.bilibili.VideoDetail;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import io.github.geniusay.crawler.util.bilibili.BilibiliUtil;
import io.github.geniusay.crawler.util.bilibili.HttpClientUtil;
import io.github.geniusay.utils.DateUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述: B站热门视频/排行榜处理器
 * @author suifeng
 * 日期: 2024/10/29
 */
public class BilibiliHotHandler {

    // 获取排行榜前100视频的URL
    private static final String HOT_RANKING_URL = "https://api.bilibili.com/x/web-interface/ranking/v2";

    /**
     * 获取B站视频排行榜前100的视频
     *
     * @param tid 分区ID，非必要参数
     * @return ApiResponse<List<VideoDetail.Data>> 前100个视频的详细信息
     */
    public static ApiResponse<List<VideoDetail>> getHotRankingVideos(Integer tid) {
        String url = HOT_RANKING_URL;
        if (tid != null) {
            url += "?tid=" + tid + "&type=all";
        } else {
            url += "?type=all";
        }

        try {
            // 发送GET请求，获取响应
            ApiResponse<String> response = HttpClientUtil.sendGetRequest(url, null);
            List<VideoDetail> videoDetails = parseHot100VideoResponse(response.getData());
            return new ApiResponse<>(response.getCode(), response.getMsg(), true, videoDetails, response.getReqTime(), response.getRespTimes(), response.getDur());
        } catch (IOException e) {
            return ApiResponse.errorResponse(e);
        }
    }

    private static List<VideoDetail> parseHot100VideoResponse(String response) {
        List<VideoDetail> videoDetailList = new ArrayList<>();

        // 解析JSON字符串
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        JsonObject data = jsonObject.getAsJsonObject("data");

        // 提取视频列表
        JsonArray videoList = data.getAsJsonArray("list");
        if (videoList != null) {
            for (int i = 0; i < videoList.size(); i++) {
                JsonObject video = videoList.get(i).getAsJsonObject();

                // 使用VideoDetail的fromJson方法来解析每个视频的JSON数据
                VideoDetail videoDetail = VideoDetail.fromJson(video);

                // 将封装好的VideoDetail对象加入列表
                videoDetailList.add(videoDetail);
            }
        }
        return videoDetailList;
    }
}