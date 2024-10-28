package io.github.geniusay.crawler.handler.bilibili;

import io.github.geniusay.crawler.po.bilibili.CommentDetail;
import io.github.geniusay.crawler.po.bilibili.CommentPage;
import io.github.geniusay.utils.DateUtil;
import io.github.geniusay.utils.HttpClientUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.FormBody;
import okhttp3.RequestBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描述: B站评论相关爬虫处理器
 * @author suifeng
 * 日期: 2024/10/28
 */
public class BilibiliCommentHandler {

    // 获取根评论的URL
    private static final String COMMENT_URL = "https://api.bilibili.com/x/v2/reply";
    // 获取根评论下回复的URL
    private static final String REPLY_URL = "https://api.bilibili.com/x/v2/reply/reply";
    // 发送评论的URL
    private static final String SEND_COMMENT_URL = "https://api.bilibili.com/x/v2/reply/add";
    // 点赞评论的URL
    private static final String LIKE_COMMENT_URL = "https://api.bilibili.com/x/v2/reply/action";
    // 点踩评论的URL
    private static final String DISLIKE_COMMENT_URL = "https://api.bilibili.com/x/v2/reply/hate";

    /**
     * 给某个评论点赞
     *
     * @param cookie 用户的Cookie
     * @param oid 视频的oid（目标评论区ID）
     * @param rpid 评论的rpid
     * @return boolean 点赞是否成功
     */
    public static boolean likeComment(String cookie, String oid, String rpid) {
        return sendLikeOrDislikeRequest(cookie, oid, rpid, LIKE_COMMENT_URL, 1);
    }

    /**
     * 给某个评论点踩
     *
     * @param cookie 用户的Cookie
     * @param oid 视频的oid（目标评论区ID）
     * @param rpid 评论的rpid
     * @return boolean 点踩是否成功
     */
    public static boolean dislikeComment(String cookie, String oid, String rpid) {
        return sendLikeOrDislikeRequest(cookie, oid, rpid, DISLIKE_COMMENT_URL, 1);
    }

    /**
     * 发送点赞或点踩的请求
     *
     * @param cookie 用户的Cookie
     * @param oid 视频的oid（目标评论区ID）
     * @param rpid 评论的rpid
     * @param url 请求的URL
     * @param action 操作代码（1表示点赞或点踩，0表示取消）
     * @return boolean 操作是否成功
     */
    private static boolean sendLikeOrDislikeRequest(String cookie, String oid, String rpid, String url, int action) {
        // 从cookie中提取csrf（bili_jct）
        String csrf = extractCsrfFromCookie(cookie);
        if (csrf == null) {
            System.out.println("无法从Cookie中提取csrf（bili_jct）。");
            return false;
        }

        // 构建POST请求的表单参数
        RequestBody formBody = new FormBody.Builder()
                .add("type", "1")  // type=1 表示视频评论区
                .add("oid", oid)    // 视频的oid
                .add("rpid", rpid)  // 评论的rpid
                .add("action", String.valueOf(action))  // 操作代码（1表示点赞或点踩，0表示取消）
                .add("csrf", csrf)  // CSRF Token
                .build();

        try {
            // 发送POST请求
            String response = HttpClientUtil.sendPostRequest(url, formBody, cookie);
            return parseLikeOrDislikeResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 解析点赞或点踩的响应
     *
     * @param response JSON响应
     * @return boolean 是否操作成功
     */
    private static boolean parseLikeOrDislikeResponse(String response) {
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        int code = jsonObject.get("code").getAsInt();
        return code == 0;  // code为0表示成功
    }

    /**
     * 发送评论或回复
     *
     * @param cookie 用户的Cookie
     * @param oid 视频的oid（目标评论区ID）
     * @param message 发送的评论内容
     * @param root 根评论的rpid（如果是回复某条评论则传递，否则为null）
     * @param parent 父评论的rpid（如果是二级或多级回复则传递，否则为null）
     * @return boolean 评论或回复是否发送成功
     */
    public static boolean sendCommentOrReply(String cookie, String oid, String message, String root, String parent) {
        // 从cookie中提取csrf（bili_jct）
        String csrf = extractCsrfFromCookie(cookie);
        if (csrf == null) {
            System.out.println("无法从Cookie中提取csrf（bili_jct）。");
            return false;
        }

        // 构建POST请求的表单参数
        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("type", "1")  // type=1 表示视频评论区
                .add("oid", oid)    // 视频的oid
                .add("message", message)  // 评论内容
                .add("plat", "1")  // plat=1 表示web端
                .add("csrf", csrf);  // CSRF Token

        // 如果是回复评论，添加root和parent参数
        if (root != null) {
            formBuilder.add("root", root);  // 根评论的rpid
        }
        if (parent != null) {
            formBuilder.add("parent", parent);  // 父评论的rpid
        }

        RequestBody formBody = formBuilder.build();

        try {
            // 发送POST请求
            String response = HttpClientUtil.sendPostRequest(SEND_COMMENT_URL, formBody, cookie);
            return parseSendCommentResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从Cookie中提取csrf（bili_jct）
     *
     * @param cookie 用户的Cookie
     * @return String 提取的csrf值
     */
    private static String extractCsrfFromCookie(String cookie) {
        // 使用正则表达式提取bili_jct的值
        Pattern pattern = Pattern.compile("bili_jct=([^;]+)");
        Matcher matcher = pattern.matcher(cookie);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;  // 如果没有找到bili_jct，返回null
    }

    /**
     * 解析发送评论的响应
     *
     * @param response JSON响应
     * @return boolean 是否发送成功
     */
    private static boolean parseSendCommentResponse(String response) {
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        int code = jsonObject.get("code").getAsInt();
        return code == 0;  // code为0表示成功
    }

    /**
     * 获取视频根评论列表
     *
     * @param cookie 用户的Cookie
     * @param oid 视频的oid（目标评论区ID）
     * @param page 页码
     * @param size 每页项数
     * @param sort 排序方式（0：按时间，1：按点赞数，2：按回复数）
     * @param nohot 是否显示热评（0：显示，1：不显示）
     * @return CommentPage 评论分页信息和列表
     */
    public static CommentPage getComments(String cookie, String oid, int page, int size, int sort, int nohot) {
        String url = COMMENT_URL + "?type=1&oid=" + oid + "&pn=" + page + "&ps=" + size + "&sort=" + sort + "&nohot=" + nohot;

        try {
            String response = HttpClientUtil.sendGetRequest(url, cookie);
            return parseCommentResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取指定根评论下的回复列表
     *
     * @param cookie 用户的Cookie
     * @param oid 视频的oid（目标评论区ID）
     * @param root 根评论的rpid
     * @param page 页码
     * @param size 每页项数
     * @return CommentPage 回复分页信息和列表
     */
    public static CommentPage getReplies(String cookie, String oid, String root, int page, int size) {
        String url = REPLY_URL + "?type=1&oid=" + oid + "&root=" + root + "&pn=" + page + "&ps=" + size;

        try {
            String response = HttpClientUtil.sendGetRequest(url, cookie);
            return parseCommentResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解析评论区响应（包括根评论和回复），提取分页信息和评论列表
     *
     * @param response JSON响应
     * @return CommentPage 评论分页信息和列表
     */
    private static CommentPage parseCommentResponse(String response) {
        CommentPage commentPage = new CommentPage();
        List<CommentDetail> comments = new ArrayList<>();
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();

        if (jsonObject.get("code").getAsInt() == 0) {
            JsonObject data = jsonObject.getAsJsonObject("data");

            // 提取分页信息
            JsonObject page = data.getAsJsonObject("page");
            commentPage.setPageNum(page.get("num").getAsInt());
            commentPage.setPageSize(page.get("size").getAsInt());
            commentPage.setTotalCount(page.get("count").getAsInt());
            commentPage.setTotalPages((int) Math.ceil((double) page.get("count").getAsInt() / page.get("size").getAsInt()));

            // 提取评论列表
            JsonArray replies = data.getAsJsonArray("replies");
            if (replies != null) {
                for (int i = 0; i < replies.size(); i++) {
                    JsonObject reply = replies.get(i).getAsJsonObject();
                    JsonObject member = reply.getAsJsonObject("member");
                    JsonObject content = reply.getAsJsonObject("content");

                    CommentDetail commentDetail = new CommentDetail();
                    commentDetail.setUsername(member.get("uname").getAsString());
                    commentDetail.setMessage(content.get("message").getAsString());
                    commentDetail.setDate(DateUtil.formatTimestamp(reply.get("ctime").getAsLong()));
                    commentDetail.setCtime(reply.get("ctime").getAsLong());
                    commentDetail.setLike(reply.get("like").getAsInt());
                    commentDetail.setRcount(reply.get("rcount").getAsInt());
                    commentDetail.setRpid(reply.get("rpid").getAsString());  // 保存rpid

                    // 获取用户等级
                    commentDetail.setLevel(member.get("level_info").getAsJsonObject().get("current_level").getAsInt());

                    comments.add(commentDetail);
                }
            }

            // 设置评论列表
            commentPage.setComments(comments);
        }

        return commentPage;
    }
}