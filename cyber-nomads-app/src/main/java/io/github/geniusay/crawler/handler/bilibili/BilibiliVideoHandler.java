package io.github.geniusay.crawler.handler.bilibili;

import io.github.geniusay.crawler.po.bilibili.VideoDetail;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import io.github.geniusay.crawler.util.bilibili.HttpClientUtil;
import io.github.geniusay.pojo.DTO.WbiSignatureResult;
import io.github.geniusay.utils.WbiSignatureUtil;
import okhttp3.FormBody;
import okhttp3.HttpUrl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static io.github.geniusay.crawler.util.bilibili.BilibiliUtil.extractCsrfFromCookie;
import static io.github.geniusay.crawler.util.bilibili.BilibiliUtil.getBooleanApiResponse;

/**
 * 描述: B站视频相关爬虫处理器
 * @author suifeng
 * 日期: 2024/10/28
 */
public class BilibiliVideoHandler {

    // 获取视频详细信息的URL
    public static final String VIDEO_DETAIL_BY_BVID = "https://api.bilibili.com/x/web-interface/view?bvid=";
    public static final String VIDEO_DETAIL_BY_AID = "https://api.bilibili.com/x/web-interface/view?aid=";

    // 点赞视频的URL
    private static final String LIKE_VIDEO_URL = "https://api.bilibili.com/x/web-interface/archive/like";
    // 投币视频的URL
    private static final String COIN_VIDEO_URL = "https://api.bilibili.com/x/web-interface/coin/add";
    // 收藏视频的URL
    private static final String FAV_VIDEO_URL = "https://api.bilibili.com/x/v3/fav/resource/deal";
    // 一键三连URL
    private static final String TRIPLE_ACTION_URL = "https://api.bilibili.com/x/web-interface/archive/like/triple";

    // AI 总结接口的URL
    private static final String AI_SUMMARY_URL = "https://api.bilibili.com/x/web-interface/view/conclusion/get";

    /**
     * 获取视频的AI总结内容
     *
     */
    public static ApiResponse<String> getVideoAiSummary(String bvid, long cid, long upMid, String imgKey, String subKey) {
        // 准备请求参数
        Map<String, Object> params = new HashMap<>();
        params.put("bvid", bvid);
        params.put("cid", cid);
        params.put("up_mid", upMid);
        params.put("web_location", 333.788D);

        WbiSignatureResult signature = WbiSignatureUtil.generateSignature(params, imgKey, subKey);

        params.put("w_rid", signature.getW_rid());
        params.put("wts", signature.getWts());

        // 构造GET请求的URL
        HttpUrl.Builder urlBuilder = HttpUrl.parse(AI_SUMMARY_URL).newBuilder();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            urlBuilder.addQueryParameter(entry.getKey(), entry.getValue().toString());
        }

        String url = urlBuilder.build().toString();

        try {
            // 发送GET请求并获取响应
            ApiResponse<String> response = HttpClientUtil.sendGetRequest(url, null);
            return response;
        } catch (IOException e) {
            return ApiResponse.errorResponse(e);
        }
    }

    /**
     * 通过bvid或aid获取视频详细信息
     *
     * @param cookie 用户的Cookie
     * @param id     视频的bvid或aid
     * @return ApiResponse<VideoDetail> 对象
     */
    public static ApiResponse<VideoDetail> getVideoDetailById(String cookie, String id) {
        String url = id.startsWith("BV") ? VIDEO_DETAIL_BY_BVID + id : VIDEO_DETAIL_BY_AID + id;

        try {
            // 发送GET请求，获取响应
            ApiResponse<String> response = HttpClientUtil.sendGetRequest(url, cookie);
            return ApiResponse.convertApiResponse(response, VideoDetail.class);
        } catch (IOException e) {
            return ApiResponse.errorResponse(e);
        }
    }

    /**
     * 点赞或取消点赞视频
     *
     * @param cookie 用户的Cookie
     * @param id 视频的bvid或aid
     * @param like 1表示点赞，2表示取消赞
     * @return ApiResponse<Boolean> 点赞或取消点赞是否成功
     */
    public static ApiResponse<Boolean> likeVideo(String cookie, String id, int like) {
        String csrf = extractCsrfFromCookie(cookie);
        if (csrf == null) {
            return ApiResponse.errorResponse("无法从Cookie中提取csrf（bili_jct）。");
        }

        // 构建POST请求的表单参数
        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("like", String.valueOf(like))
                .add("csrf", csrf);

        if (id.startsWith("BV")) {
            formBuilder.add("bvid", id);
        } else {
            formBuilder.add("aid", id);
        }

        return getBooleanApiResponse(cookie, formBuilder, LIKE_VIDEO_URL);
    }

    /**
     * 为视频投币
     *
     * @param cookie 用户的Cookie
     * @param id 视频的bvid或aid
     * @param multiply 投币数量，最大为2
     * @param selectLike 是否同时点赞，0表示不点赞，1表示同时点赞
     * @return ApiResponse<Boolean> 投币是否成功
     */
    public static ApiResponse<Boolean> coinVideo(String cookie, String id, int multiply, int selectLike) {
        String csrf = extractCsrfFromCookie(cookie);
        if (csrf == null) {
            return ApiResponse.errorResponse("无法从Cookie中提取csrf（bili_jct）。");
        }

        // 构建POST请求的表单参数
        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("multiply", String.valueOf(multiply))
                .add("select_like", String.valueOf(selectLike))
                .add("csrf", csrf);

        if (id.startsWith("BV")) {
            formBuilder.add("bvid", id);
        } else {
            formBuilder.add("aid", id);
        }

        return getBooleanApiResponse(cookie, formBuilder, COIN_VIDEO_URL);
    }

    /**
     * 收藏或取消收藏视频
     *
     * @param cookie 用户的Cookie
     * @param rid 视频的aid
     * @param addMediaIds 要加入的收藏夹ID
     * @param delMediaIds 要取消的收藏夹ID（可选）
     * @return ApiResponse<Boolean> 收藏或取消收藏是否成功
     */
    public static ApiResponse<Boolean> favVideo(String cookie, String rid, String addMediaIds, String delMediaIds) {
        String csrf = extractCsrfFromCookie(cookie);
        if (csrf == null) {
            return ApiResponse.errorResponse("无法从Cookie中提取csrf（bili_jct）。");
        }

        // 构建POST请求的表单参数
        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("rid", rid)
                .add("type", "2")
                .add("csrf", csrf);

        if (addMediaIds != null && !addMediaIds.isEmpty()) {
            formBuilder.add("add_media_ids", addMediaIds);
        }
        if (delMediaIds != null && !delMediaIds.isEmpty()) {
            formBuilder.add("del_media_ids", delMediaIds);
        }

        return getBooleanApiResponse(cookie, formBuilder, FAV_VIDEO_URL);
    }

    /**
     * 一键三连（点赞、投币、收藏）
     *
     * @param cookie 用户的Cookie
     * @param id 视频的bvid或aid
     * @return ApiResponse<Boolean> 一键三连是否成功
     */
    public static ApiResponse<Boolean> tripleAction(String cookie, String id) {
        String csrf = extractCsrfFromCookie(cookie);
        if (csrf == null) {
            return ApiResponse.errorResponse("无法从Cookie中提取csrf（bili_jct）。");
        }

        // 构建POST请求的表单参数
        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("csrf", csrf);

        if (id.startsWith("BV")) {
            formBuilder.add("bvid", id);
        } else {
            formBuilder.add("aid", id);
        }

        return getBooleanApiResponse(cookie, formBuilder, TRIPLE_ACTION_URL);
    }
}
