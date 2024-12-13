package io.github.geniusay.crawler.test.bilibili;

import io.github.geniusay.crawler.api.bilibili.BilibiliUserApi;
import io.github.geniusay.crawler.po.bilibili.UserInfo;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import org.junit.Test;

import static io.github.geniusay.crawler.BCookie.cookie;

public class TestUserApi {

    /**
     * 测试单个用户关注功能
     */
    @Test
    public void testFollowUser() {
        // 目标用户的mid
        String fid = "3546382956759627";
        // 关注来源代码（例如：11表示空间，14表示视频，115表示文章）
        int reSrc = 11;
        ApiResponse<Boolean> response = BilibiliUserApi.followUser(cookie, fid, reSrc);
        if (response.isSuccess()) {
            System.out.println("关注成功！");
        } else {
            System.out.println("关注失败，错误信息: " + response.getMsg());
        }
    }

    /**
     * 测试批量关注用户功能
     */
    @Test
    public void testBatchFollowUsers() {
        String fids = "33983241,10272440,395991094";
        int reSrc = 11;
        // 调用批量关注用户的API
        ApiResponse<Boolean> response = BilibiliUserApi.batchFollowUsers(cookie, fids, reSrc);
        // 检查响应结果
        if (response.isSuccess()) {
            System.out.println("批量关注成功！");
        } else {
            System.out.println("批量关注失败，错误信息: " + response.getMsg());
        }
    }

    @Test
    public void testGetUserInfo() {
        ApiResponse<UserInfo> userInfo = BilibiliUserApi.getUserInfo(cookie);
        if (userInfo.isSuccess()) {
            UserInfo data = userInfo.getData();
            System.out.println(data.getData());
        }
    }
}