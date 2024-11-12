package io.github.geniusay.crawler.api.bilibili;

import io.github.geniusay.crawler.handler.bilibili.BilibiliUserHandler;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;

/**
 * 描述: B站用户操作相关API
 * 实现用户的关注和批量关注功能
 */
public class BilibiliUserApi {

    /**
     * 关注用户
     * @param cookie 用户的Cookie
     * @param fid 目标用户的mid
     * @param reSrc 关注来源代码 (如：11表示空间，14表示视频，文章：115，活动页面：222)
     * @return ApiResponse<Boolean> 是否关注成功
     */
    public static ApiResponse<Boolean> followUser(String cookie, String fid, int reSrc) {
        return BilibiliUserHandler.modifyUserRelation(cookie, fid, 1, reSrc);
    }

    /**
     * 批量关注用户
     * @param cookie 用户的Cookie
     * @param fids 目标用户的mid列表（用逗号分隔）
     * @param reSrc 关注来源代码 (如：11表示空间，14表示视频，文章：115，活动页面：222)
     * @return ApiResponse<Boolean> 是否批量关注成功
     */
    public static ApiResponse<Boolean> batchFollowUsers(String cookie, String fids, int reSrc) {
        return BilibiliUserHandler.batchModifyUserRelation(cookie, fids, 1, reSrc);
    }
}