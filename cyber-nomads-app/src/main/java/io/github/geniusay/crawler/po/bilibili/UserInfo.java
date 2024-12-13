package io.github.geniusay.crawler.po.bilibili;

import lombok.Data;


/**
 * 描述: 用户信息数据模型
 * @author 
 * 日期: 2024/12/13
 */
@Data
public class UserInfo {

    private int code;
    private String message;
    private int ttl;
    private Data data;

    @lombok.Data
    public static class Data {
        private boolean isLogin; // 是否已登录
        private String face; // 用户头像URL
        private LevelInfo level_info; // 等级信息
        private long mid; // 用户ID
        private String uname; // 用户昵称
        private WbiImg wbi_img; // Wbi签名实时口令

        // 嵌套对象：等级信息
        @lombok.Data
        public static class LevelInfo {
            private int current_level; // 当前等级
            private int current_exp; // 当前经验
            private String next_exp; // 下一级所需经验
        }

        // 嵌套对象：Wbi签名信息
        @lombok.Data
        public static class WbiImg {
            private String img_url; // imgKey伪装URL
            private String sub_url; // subKey伪装URL
        }
    }
}