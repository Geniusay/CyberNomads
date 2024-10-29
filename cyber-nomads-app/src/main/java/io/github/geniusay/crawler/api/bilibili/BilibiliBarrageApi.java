package io.github.geniusay.crawler.api.bilibili;

import io.github.geniusay.crawler.handler.bilibili.BilibiliBarrageHandler;
import io.github.geniusay.crawler.po.bilibili.Barrage;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;

import java.util.List;

/**
 * 描述: B站弹幕相关API
 * @author suifeng
 * 日期: 2024/10/29
 */
public class BilibiliBarrageApi {

    /**
     * 获取视频的实时弹幕池弹幕
     *
     * @param cid 视频的cid / 弹幕池id
     * @return List<Barrage> 弹幕列表
     */
    public static ApiResponse<List<Barrage>> getRealTimeBarrageByCid(String cookie, String cid) {
        return BilibiliBarrageHandler.getRealTimeBarrageByCid(cookie, cid);
    }
}
