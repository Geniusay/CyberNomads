package io.github.geniusay.crawler.api.bilibili;

import io.github.geniusay.crawler.handler.bilibili.BilibiliBarrageHandler;
import io.github.geniusay.crawler.po.bilibili.Barrage;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;

import java.util.List;

/**
 * 描述: B站弹幕相关API
 *
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


    /**
     * 发送弹幕
     *
     * @param cookie   用户的Cookie
     * @param cid      视频的cid
     * @param message  弹幕内容
     * @param progress 弹幕出现的时间（毫秒）
     * @param color    弹幕颜色（RGB十进制值）
     * @param fontsize 弹幕字体大小
     * @param mode     弹幕类型
     * @return ApiResponse<Boolean> 发送是否成功
     */
    public static ApiResponse<Boolean> sendBarrage(String cookie, String aid, String cid, String message, int progress, int color, int fontsize, int mode, String imgKey, String subKey) {
        return BilibiliBarrageHandler.sendBarrage(cookie, aid, cid, message, progress, color, fontsize, mode, imgKey, subKey);
    }
}
