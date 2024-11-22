package io.github.geniusay.crawler.handler.bilibili;

import io.github.geniusay.crawler.po.bilibili.VideoSearchResult;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import io.github.geniusay.crawler.util.bilibili.HttpClientUtil;
import io.github.geniusay.pojo.DTO.WbiSignatureResult;
import io.github.geniusay.utils.WbiSignatureUtil;
import okhttp3.HttpUrl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述: B站搜索相关爬虫处理器
 * @author suifeng
 * 日期: 2024/10/28
 */
public class BilibiliSearchHandler {

    // B站搜索API的基础URL
    private static final String SEARCH_URL = "https://api.bilibili.com/x/web-interface/search/type";

    /**
     * 按关键词搜索视频
     * @param keyword  搜索关键词
     * @param order    排序方式 (如: totalrank, click, pubdate 等)
     * @param duration 视频时长筛选 (0: 全部, 1: 10分钟以下, 2: 10-30分钟, 3: 30-60分钟, 4: 60分钟以上)
     * @param tids     视频分区筛选 (0: 全部分区, 具体分区ID参考文档)
     * @param page     页码
     * @param page_size     每页条数
     * @param imgKey   用于生成签名的 img_key
     * @param subKey   用于生成签名的 sub_key
     * @return ApiResponse<VideoSearchResult> 包含搜索结果的响应对象
     */
    public static ApiResponse<VideoSearchResult> searchVideos(String keyword, String order, int duration, int tids, int page, int page_size, String imgKey, String subKey) {
        if (page_size > 50) {
            page_size = 50;
        }
        // 准备请求参数
        Map<String, Object> params = new HashMap<>();
        params.put("search_type", "video");
        params.put("keyword", keyword);
        params.put("order", order != null ? order : "totalrank");
        params.put("duration", duration);
        params.put("tids", tids);
        params.put("page", page);
        params.put("page_size", page_size);

        // 生成 w_rid 和 wts 参数
        WbiSignatureResult signature = WbiSignatureUtil.generateSignature(params, imgKey, subKey);
        params.put("w_rid", signature.getW_rid());
        params.put("wts", signature.getWts());
        HttpUrl.Builder urlBuilder = HttpUrl.parse(SEARCH_URL).newBuilder();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            urlBuilder.addQueryParameter(entry.getKey(), entry.getValue().toString());
        }
        String url = urlBuilder.build().toString();
        try {
            ApiResponse<String> response = HttpClientUtil.sendGetRequest(url, null);
            return ApiResponse.convertApiResponse(response, VideoSearchResult.class);
        } catch (IOException e) {
            return ApiResponse.errorResponse(e);
        }
    }
}