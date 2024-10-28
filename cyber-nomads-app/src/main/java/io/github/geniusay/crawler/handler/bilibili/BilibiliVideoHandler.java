package io.github.geniusay.crawler.handler.bilibili;

import io.github.geniusay.crawler.po.bilibili.VideoDetail;
import io.github.geniusay.utils.HttpClientUtil;
import okhttp3.FormBody;
import okhttp3.RequestBody;

import java.io.IOException;

import static io.github.geniusay.crawler.util.bilibili.BilibiliUtils.extractCsrfFromCookie;
import static io.github.geniusay.crawler.util.bilibili.BilibiliUtils.parseVideoResponse;

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

    /**
     * 通过bvid或aid获取视频详细信息
     *
     * @param cookie 用户的Cookie
     * @param id 视频的bvid或aid
     * @return VideoDetail 对象
     */
    public static VideoDetail getVideoDetailById(String cookie, String id) {
        String url;

        // 判断id是否以"BV"开头
        if (id.startsWith("BV")) {
            url = VIDEO_DETAIL_BY_BVID + id;
        } else {
            url = VIDEO_DETAIL_BY_AID + id;
        }

        try {
            String response = HttpClientUtil.sendGetRequest(url, cookie);
            return HttpClientUtil.parseJson(response, VideoDetail.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 点赞或取消点赞视频
     *
     * @param cookie 用户的Cookie（SESSDATA）
     * @param id 视频的bvid或aid
     * @param like 1表示点赞，2表示取消赞
     * @return boolean 点赞或取消点赞是否成功
     */
    public static boolean likeVideo(String cookie, String id, int like) {
        // 从cookie中提取csrf（bili_jct）
        String csrf = extractCsrfFromCookie(cookie);
        if (csrf == null) {
            System.out.println("无法从Cookie中提取csrf（bili_jct）。");
            return false;
        }

        // 构建POST请求的表单参数
        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("like", String.valueOf(like))  // 点赞或取消赞
                .add("csrf", csrf);  // CSRF Token

        // 根据id判断是bvid还是aid
        if (id.startsWith("BV")) {
            formBuilder.add("bvid", id);
        } else {
            formBuilder.add("aid", id);
        }

        RequestBody formBody = formBuilder.build();

        try {
            // 发送POST请求
            String response = HttpClientUtil.sendPostRequest(LIKE_VIDEO_URL, formBody, cookie);
            return parseVideoResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 为视频投币
     *
     * @param cookie 用户的Cookie
     * @param id 视频的bvid或aid
     * @param multiply 投币数量，最大为2
     * @param selectLike 是否同时点赞，0表示不点赞，1表示同时点赞
     * @return boolean 投币是否成功
     */
    public static boolean coinVideo(String cookie, String id, int multiply, int selectLike) {
        // 从cookie中提取csrf（bili_jct）
        String csrf = extractCsrfFromCookie(cookie);
        if (csrf == null) {
            System.out.println("无法从Cookie中提取csrf（bili_jct）。");
            return false;
        }

        // 构建POST请求的表单参数
        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("multiply", String.valueOf(multiply))
                .add("select_like", String.valueOf(selectLike))
                .add("csrf", csrf);

        // 根据id判断是bvid还是aid
        if (id.startsWith("BV")) {
            formBuilder.add("bvid", id);
        } else {
            formBuilder.add("aid", id);
        }

        RequestBody formBody = formBuilder.build();

        try {
            // 发送POST请求
            String response = HttpClientUtil.sendPostRequest(COIN_VIDEO_URL, formBody, cookie);
            return parseVideoResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 收藏或取消收藏视频
     *
     * @param cookie 用户的Cookie
     * @param rid 视频的aid
     * @param addMediaIds 要加入的收藏夹ID
     * @param delMediaIds 要取消的收藏夹ID（可选）
     * @return boolean 收藏或取消收藏是否成功
     */
    public static boolean favVideo(String cookie, String rid, String addMediaIds, String delMediaIds) {
        // 从cookie中提取csrf（bili_jct）
        String csrf = extractCsrfFromCookie(cookie);
        if (csrf == null) {
            System.out.println("无法从Cookie中提取csrf（bili_jct）。");
            return false;
        }

        // 构建POST请求的表单参数
        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("rid", rid)
                .add("type", "2")
                .add("csrf", csrf);

        // 添加或取消收藏夹
        if (addMediaIds != null && !addMediaIds.isEmpty()) {
            formBuilder.add("add_media_ids", addMediaIds);
        }
        if (delMediaIds != null && !delMediaIds.isEmpty()) {
            formBuilder.add("del_media_ids", delMediaIds);
        }

        RequestBody formBody = formBuilder.build();

        try {
            // 发送POST请求
            String response = HttpClientUtil.sendPostRequest(FAV_VIDEO_URL, formBody, cookie);
            return parseVideoResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
