package io.github.geniusay.crawler.handler.bilibili;

import io.github.geniusay.crawler.po.bilibili.Barrage;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import io.github.geniusay.crawler.util.bilibili.BilibiliUtil;
import io.github.geniusay.crawler.util.bilibili.HttpClientUtil;
import io.github.geniusay.pojo.DTO.WbiSignatureResult;
import io.github.geniusay.utils.WbiSignatureUtil;
import okhttp3.FormBody;
import okhttp3.HttpUrl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.geniusay.crawler.util.bilibili.BilibiliUtil.extractCsrfFromCookie;

/**
 * 描述: B站弹幕相关爬虫处理器
 * 日期: 2024/10/29
 */
public class BilibiliBarrageHandler {

    // 实时弹幕的URL
    private static final String REAL_TIME_BARRAGE_URL = "https://comment.bilibili.com/";

    // 发送弹幕的URL
    private static final String SEND_BARRAGE_URL = "https://api.bilibili.com/x/v2/dm/post";

    /**
     * 发送弹幕
     *
     * @param cookie 用户的Cookie
     * @param cid 视频的cid
     * @param message 弹幕内容
     * @param progress 弹幕出现的时间（毫秒）
     * @param color 弹幕颜色（RGB十进制值）
     * @param fontsize 弹幕字体大小
     * @param mode 弹幕类型
     * @param imgKey Wbi 签名的 imgKey
     * @param subKey Wbi 签名的 subKey
     * @return ApiResponse<Boolean> 发送是否成功
     */
    public static ApiResponse<Boolean> sendBarrage(String cookie, String aid, String cid, String message, int progress, int color, int fontsize, int mode, String imgKey, String subKey) {
        // 从Cookie中提取csrf
        String csrf = extractCsrfFromCookie(cookie);
        if (csrf == null) {
            return ApiResponse.errorResponse("无法从Cookie中提取csrf（bili_jct）。");
        }

        // 准备请求参数
        Map<String, Object> params = new HashMap<>();
        params.put("csrf", csrf);
        params.put("type", "1");  // 弹幕类型固定为视频弹幕
        params.put("oid", cid);  // 视频的cid
        params.put("msg", message);  // 弹幕内容
        params.put("aid", aid);
        params.put("progress", String.valueOf(progress));  // 弹幕出现的时间（毫秒）
        params.put("color", String.valueOf(color));  // 弹幕颜色
        params.put("fontsize", String.valueOf(fontsize));  // 弹幕字体大小
        params.put("pool", "0");  // 弹幕池固定为普通池
        params.put("mode", String.valueOf(mode));  // 弹幕类型
        params.put("rnd", "2");  // 弹幕冷却时间戳

        // 使用 WbiSignatureUtil 生成 w_rid 和 wts
        WbiSignatureResult signature = WbiSignatureUtil.generateSignature(params, imgKey, subKey);
        long wts = signature.getWts();
        String wRid = signature.getW_rid();

        // 构造带有 w_rid 和 wts 的 URL
        HttpUrl.Builder urlBuilder = HttpUrl.parse(SEND_BARRAGE_URL).newBuilder()
                .addQueryParameter("csrf", csrf)
                .addQueryParameter("w_rid", wRid)
                .addQueryParameter("wts", String.valueOf(wts));

        String url = urlBuilder.build().toString();

        // 构建POST请求的表单参数
        FormBody formBody = new FormBody.Builder()
                .add("type", "1")  // 弹幕类型固定为视频弹幕
                .add("oid", String.valueOf(cid))  // 视频的cid
                .add("msg", message)  // 弹幕内容
                .add("aid", aid)
                .add("progress", String.valueOf(progress))  // 弹幕出现的时间（毫秒）
                .add("color", String.valueOf(color))  // 弹幕颜色
                .add("fontsize", String.valueOf(fontsize))  // 弹幕字体大小
                .add("pool", "0")  // 弹幕池固定为普通池
                .add("mode", String.valueOf(mode))  // 弹幕类型
                .add("rnd", "2")  // 弹幕冷却时间戳
                .add("csrf", csrf)
                .build();

        try {
            // 发送POST请求
            ApiResponse<String> response = HttpClientUtil.sendPostRequest(url, formBody, cookie);
            return ApiResponse.handleApiResponse(response, Boolean.class, r -> true);
        } catch (IOException e) {
            return ApiResponse.errorResponse(e);
        }
    }

    /**
     * 获取视频的实时弹幕池弹幕
     *
     * @param cid 视频的cid / 弹幕池id
     * @return ApiResponse<List<Barrage>> 弹幕列表
     */
    public static ApiResponse<List<Barrage>> getRealTimeBarrageByCid(String cookie, String cid) {
        String url = REAL_TIME_BARRAGE_URL + cid + ".xml";

        try {
            // 获取弹幕的原始XML数据
            ApiResponse<String> response = HttpClientUtil.sendGetRequestWithDeflate(url, cookie);
            return new ApiResponse<>(response.getCode(), response.getMsg(), true, BilibiliUtil.parseBarrageXml(response.getData()), response.getReqTime(), response.getRespTimes(), response.getDur());
        } catch (IOException e) {
            // 捕获异常并返回错误的 ApiResponse
            return ApiResponse.errorResponse(e);
        }
    }
}