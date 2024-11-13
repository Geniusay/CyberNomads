package io.github.geniusay.core.supertask.config;

public class PluginConstant {

    /**
     * 插件组名称
     */
    public static final String TERMINATOR_GROUP_NAME = "terminator";  // 任务终止器组
    public static final String COMMENT_GROUP_NAME = "comment";  // 评论生成器组
    public static final String GET_VIDEO_GROUP_NAME = "getVideo";  // 视频选择器组
    public static final String LOGIC_SELECTOR_GROUP_NAME = "logicSelector";  // 操作选择器组
    public static final String RECEIVER_SELECTOR_GROUP_NAME = "receiverSelector";  // 目标选择器组

    /**
     * 插件名称
     */
    // 评论生成器插件
    public static final String AI_COMMENT_GENERATE_PLUGIN = "aiComment";  // AI自动评论

    // 视频选择器插件
    public static final String HOT_VIDEO_PLUGIN = "hotVideo";  // 随机热门视频

    // 操作选择器插件
    public static final String LIKE_LOGIC_SELECTOR = "likeLogicSelector";  // 点赞操作
    public static final String TRIPLET_LOGIC_SELECTOR = "tripletLogicSelector";  // 三连操作
    public static final String COIN_LOGIC_SELECTOR = "coinLogicSelector";  // 投币操作
    public static final String FOLLOW_LOGIC_SELECTOR = "followLogicSelector";  // 关注操作

    // 目标选择器插件
    public static final String USER_RECEIVER_SELECTOR = "userReceiverSelector";  // 用户目标选择
    public static final String VIDEO_RECEIVER_SELECTOR = "videoReceiverSelector";  // 视频目标选择
    public static final String COMMENT_RECEIVER_SELECTOR = "commentReceiverSelector";  // 评论目标选择
}