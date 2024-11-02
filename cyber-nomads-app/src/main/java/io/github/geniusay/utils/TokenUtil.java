package io.github.geniusay.utils;

import cn.dev33.satoken.stp.StpUtil;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/27 22:23
 */
public class TokenUtil {
    public static String getToken(String id, String username, String nickname){
        String loginId = id + "|" + username + "|" + nickname;
        StpUtil.login(loginId);
        return StpUtil.getTokenValueByLoginId(loginId);
    }

    public static void logout(String id, String username, String nickname){
        StpUtil.logout(id + "|" + username + "|" + nickname);
    }
}
