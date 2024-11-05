package io.github.geniusay.utils;

import static io.github.geniusay.constants.LastWordConstants.ERROR;
import static io.github.geniusay.constants.LastWordConstants.SUCCESS;

/**
 * 遗言工具类
 */
public class LastWordUtil {

    /**
     * 构建规范的遗言
     * @param msg 遗言内容
     * @param isSuccess 是否是成功
     * @return string
     */
    public static String buildLastWord(String msg, boolean isSuccess){
        return (isSuccess?SUCCESS:ERROR)+msg;
    }

    /**
     * 根据结果是否成功，选择性返回成功遗言和失败遗言
     * @param successMsg 成功遗言
     * @param errorMsg 失败遗言
     * @param isSuccess 是否成功
     * @return string
     */
    public static String chooseLastWordByRes(String successMsg, String errorMsg, boolean isSuccess){
        return buildLastWord(isSuccess?successMsg:errorMsg, isSuccess);
    }

    /**
     * 判断遗言类型
     * @param msg 内容
     * @return 是否成功
     */
    public static boolean isSuccess(String msg){
        return msg.startsWith(SUCCESS);
    }
}
