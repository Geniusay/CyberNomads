package io.github.geniusay.crawler.handler.bilibili;

import io.github.geniusay.crawler.po.bilibili.UserInfo;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import io.github.geniusay.crawler.util.bilibili.HttpClientUtil;
import okhttp3.FormBody;

import java.io.IOException;

import static io.github.geniusay.crawler.util.bilibili.BilibiliUtil.extractCsrfFromCookie;
import static io.github.geniusay.crawler.util.bilibili.BilibiliUtil.getBooleanApiResponse;

/**
 * 描述: B站用户操作相关处理器
 * 负责处理用户的关注和批量关注操作
 */
public class BilibiliUserHandler {

    // 关注用户的URL
    private static final String MODIFY_USER_RELATION_URL = "https://api.bilibili.com/x/relation/modify";
    // 批量关注用户的URL
    private static final String BATCH_MODIFY_USER_RELATION_URL = "https://api.bilibili.com/x/relation/batch/modify";
    // 用户信息接口URL
    private static final String USER_INFO_URL = "https://api.bilibili.com/x/web-interface/nav";

    /**
     * 获取用户信息
     *
     * @param cookie 用户的Cookie，必须包含SESSDATA
     * @return ApiResponse<UserInfo> 包含用户信息的响应
     */
    public static ApiResponse<UserInfo> getUserInfo(String cookie) {
        String csrf = extractCsrfFromCookie(cookie);
        if (csrf == null) {
            return ApiResponse.errorResponse("无法从Cookie中提取csrf（bili_jct）。");
        }
        try {
            ApiResponse<String> response = HttpClientUtil.sendGetRequest(USER_INFO_URL, cookie);
            return ApiResponse.convertApiResponse(response, UserInfo.class);
        } catch (IOException e) {
            return ApiResponse.errorResponse(e);
        }
    }

    /**
     * 修改用户关系（关注/取关/拉黑等操作）
     * @param cookie 用户的Cookie
     * @param fid 目标用户的mid
     * @param act 操作代码（1：关注，2：取关，5：拉黑，6：取消拉黑）
     * @param reSrc 关注来源代码 (如：11表示空间，14表示视频，文章：115，活动页面：222)
     * @return ApiResponse<Boolean> 操作是否成功
     */
    public static ApiResponse<Boolean> modifyUserRelation(String cookie, String fid, int act, int reSrc) {
        String csrf = extractCsrfFromCookie(cookie);
        if (csrf == null) {
            return ApiResponse.errorResponse("无法从Cookie中提取csrf（bili_jct）。");
        }
        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("fid", fid)
                .add("act", String.valueOf(act))
                .add("re_src", String.valueOf(reSrc))
                .add("csrf", csrf);

        return getBooleanApiResponse(cookie, formBuilder, MODIFY_USER_RELATION_URL);
    }

    /**
     * 批量修改用户关系（批量关注/拉黑操作）
     * @param cookie 用户的Cookie
     * @param fids 目标用户的mid列表（用逗号分隔）
     * @param act 操作代码（1：关注，5：拉黑）
     * @param reSrc 关注来源代码 (如：11表示空间，14表示视频，文章：115，活动页面：222)
     * @return ApiResponse<Boolean> 操作是否成功
     */
    public static ApiResponse<Boolean> batchModifyUserRelation(String cookie, String fids, int act, int reSrc) {
        String csrf = extractCsrfFromCookie(cookie);
        if (csrf == null) {
            return ApiResponse.errorResponse("无法从Cookie中提取csrf（bili_jct）。");
        }
        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("fids", fids)
                .add("act", String.valueOf(act))
                .add("re_src", String.valueOf(reSrc))
                .add("csrf", csrf);
        return getBooleanApiResponse(cookie, formBuilder, BATCH_MODIFY_USER_RELATION_URL);
    }
}