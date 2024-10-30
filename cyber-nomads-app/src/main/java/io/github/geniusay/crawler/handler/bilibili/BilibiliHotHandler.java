package io.github.geniusay.crawler.handler.bilibili;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.geniusay.crawler.po.bilibili.*;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import io.github.geniusay.crawler.util.bilibili.HttpClientUtil;
import org.jetbrains.annotations.NotNull;

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

    // 获取当前热门视频的URL
    private static final String POPULAR_VIDEO_URL = "https://api.bilibili.com/x/web-interface/popular";

    // 每周必看全部列表的URL
    private static final String WEEKLY_SERIES_URL = "https://api.bilibili.com/x/web-interface/popular/series/list";

    // 获取每周必看选期详细信息的URL
    private static final String SERIES_DETAIL_URL = "https://api.bilibili.com/x/web-interface/popular/series/one";

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

        return getListApiResponse(url);
    }

    /**
     * 获取当前热门视频列表
     *
     * @param pn 页码
     * @param ps 每页项数
     * @return ApiResponse<List<VideoDetail>> 当前热门视频列表
     */
    public static ApiResponse<List<VideoDetail>> getPopularVideos(int pn, int ps) {
        String url = POPULAR_VIDEO_URL + "?pn=" + pn + "&ps=" + ps;

        return getListApiResponse(url);
    }

    /**
     * 获取每周必看全部列表
     *
     * @return ApiResponse<List<WeeklySeries>> 每周必看全部列表
     */
    public static ApiResponse<List<WeeklySeries>> getWeeklySeriesList() {
        try {
            // 发送GET请求，获取响应
            ApiResponse<String> response = HttpClientUtil.sendGetRequest(WEEKLY_SERIES_URL, null);
            List<WeeklySeries> seriesList = parseWeeklySeriesResponse(response.getData());
            return new ApiResponse<>(response.getCode(), response.getMsg(), true, seriesList, response.getReqTime(), response.getRespTimes(), response.getDur());
        } catch (IOException e) {
            return ApiResponse.errorResponse(e);
        }
    }

    /**
     * 获取每周必看选期详细信息
     *
     * @param cookie
     * @param number 期数
     * @return ApiResponse<SeriesDetail> 选期详细信息
     */
    public static ApiResponse<SeriesDetail> getSeriesDetail(String cookie, int number) {
        String url = SERIES_DETAIL_URL + "?number=" + number;

        try {
            // 发送GET请求，获取响应
            ApiResponse<String> response = HttpClientUtil.sendGetRequest(url, cookie);
            if (response.getCode() == 0) {
                SeriesDetail seriesDetail = parseSeriesDetailResponse(response.getData());
                return new ApiResponse<>(response.getCode(), response.getMsg(), true, seriesDetail, response.getReqTime(), response.getRespTimes(), response.getDur());
            } else {
                return new ApiResponse<>(response.getCode(), response.getMsg(), false, null, response.getReqTime(), response.getRespTimes(), response.getDur());
            }
        } catch (IOException e) {
            return ApiResponse.errorResponse(e);
        }
    }

    /**
     * 解析选期详细信息的 JSON 响应
     *
     * @param response JSON 响应字符串
     * @return SeriesDetail 转换后的选期详细信息
     */
    private static SeriesDetail parseSeriesDetailResponse(String response) {
        // 解析JSON字符串
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        JsonObject data = jsonObject.getAsJsonObject("data");

        // 使用SeriesDetail的fromJson方法解析选期详细信息
        return SeriesDetail.fromJson(data);
    }

    /**
     * 解析每周必看列表的 JSON 响应
     *
     * @param response JSON 响应字符串
     * @return List<WeeklySeries> 转换后的每周必看列表
     */
    private static List<WeeklySeries> parseWeeklySeriesResponse(String response) {
        List<WeeklySeries> seriesList = new ArrayList<>();

        // 解析JSON字符串
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        JsonObject data = jsonObject.getAsJsonObject("data");

        // 提取期数列表
        JsonArray list = data.getAsJsonArray("list");
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                JsonObject seriesJson = list.get(i).getAsJsonObject();
                // 使用 WeeklySeries 的 fromJson 方法解析每个期数的 JSON 数据
                WeeklySeries series = WeeklySeries.fromJson(seriesJson);
                seriesList.add(series);
            }
        }
        return seriesList;
    }

    @NotNull
    public static ApiResponse<List<VideoDetail>> getListApiResponse(String url) {
        try {
            // 发送GET请求，获取响应
            ApiResponse<String> response = HttpClientUtil.sendGetRequest(url, null);
            List<VideoDetail> videoDetails = parseVideoResponse(response.getData());
            return new ApiResponse<>(response.getCode(), response.getMsg(), true, videoDetails, response.getReqTime(), response.getRespTimes(), response.getDur());
        } catch (IOException e) {
            return ApiResponse.errorResponse(e);
        }
    }

    private static List<VideoDetail> parseVideoResponse(String response) {
        List<VideoDetail> videoDetailList = new ArrayList<>();

        // 解析JSON字符串
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        JsonObject data = jsonObject.getAsJsonObject("data");

        // 提取视频列表
        JsonArray videoList = data.getAsJsonArray("list");
        if (videoList != null) {
            for (int i = 0; i < videoList.size(); i++) {
                JsonObject video = videoList.get(i).getAsJsonObject();
                VideoDetail videoDetail = VideoDetail.fromJson(video);
                // 将封装好的VideoDetail对象加入列表
                videoDetailList.add(videoDetail);
            }
        }
        return videoDetailList;
    }
}