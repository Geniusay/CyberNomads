package io.github.geniusay.crawler.handler.bilibili;

import io.github.geniusay.crawler.po.bilibili.Barrage;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import io.github.geniusay.crawler.util.bilibili.BilibiliUtil;
import io.github.geniusay.crawler.util.bilibili.HttpClientUtil;

import java.io.IOException;
import java.util.List;

/**
 * 描述: B站弹幕相关爬虫处理器
 * 日期: 2024/10/29
 */
public class BilibiliBarrageHandler {

    // 实时弹幕的URL
    private static final String REAL_TIME_BARRAGE_URL = "https://comment.bilibili.com/";

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