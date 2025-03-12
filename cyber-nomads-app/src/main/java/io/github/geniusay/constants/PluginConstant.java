package io.github.geniusay.constants;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/4 2:00
 */
public class PluginConstant {

    public static final String BASED_ON_CONTENT = "basedOnContent";

    public static final String AI_PRE_TEXT = "aiPreText";

    public static final String AI_COUNT_NUM = "aiCountNum";

    public static final String CUSTOM_COMMENT_CONTENT = "customCommentContent";

    public static final String KEYWORD_SEARCH = "keywordSearch";
    public static final String KEYWORD_SEARCH_ORDER = "keywordSearchOrder";

    public static final String SLOGAN = "slogan";

    public static final String SCOPE = "scope";

    public static final String LINK_OR_BV = "linkOrBV";

    public static final String COIN_SUM = "coinSum";

    public static final String RE_SRC = "reSrc";

    public static final String AND_LIKE = "andLike";

    public static final String UID = "uid";

    public static final String LOGIC_NAME = "logicName";

    public static final String RECEIVER_NAME = "receiverName";

    public static final String LOGIC_CONTENT = "logicContent";

    public static final String AI_MODEL = "aiModel";


    // extendDesc常量
    public static final String PARAM_COOLDOWN_TIME_EXT_DESC = "冷却时间：指的是每一个robot做一次该任务，需要等待的时间，每个robot单独计算冷却时间。";
    public static final String SINGLE_PARAM_COOLDOWN_TIME_EXT_DESC = "冷却时间：当前任务每次执行间隔的时间。";
    public static final String AI_COUNT_NUM_EXT_DESC = "AI生成内容的最大上限值，生成字数不会超过这个值。";
    public static final String SLOGAN_EXT_DESC = "这是一段固定语句，会拼接在生成的AI内容最后。";

    public static final String RE_SRC_EXT_DESC = "代表你是从何处关注该用户的，会提升关注来源处的某些权重。例如：从视频来源关注的用户，可能会提高视频的推荐权重。";
    public static final String UID_EXT_DESC = "B站用户的uid，可以点击用户头像去用户的空间找到。";
    public static final String AND_LIKE_EXT_DESC = "投币的时候是否同时点赞用户。";

    public static final String CUSTOM_COMMENT_CONTENT_EXT_DESC = "参考用例：你好###你好吗###我很好，将被分割为三句评论【你好】【你好吗】【我很好】 赛博游民会轮流挑选这三条评论进行宣传";
    public static final String SCOPE_DESC = "这是一种避免 赛博游民 挑选到重复热门视频的一个规则。任务级别：单个任务下的某个机器人不会挑选到已访问过的视频，" +
            "机器人级别：在账号下进行的任务中，机器人不会挑选到已访问过视频";

    public static final String KEYWORD_SEARCH_DESC = "参考用例：【性感猫娘】【变态教父】【嗨丝诱惑】，将被分割为三个关键词，赛博游民会找到这些关键词对应的视频；注意：(最多输入10个关键词，多输无效)";

    public static final String KEYWORD_SEARCH_ORDER_DESC = "【综合排序】：根据b站默认规则选取；【最多播放】：选择播放量高的视频；【最新发布】：选取最近新发布的视频";


    public static final String AI_MODEL_DESC = "AI将使用以下模型进行回答";
}
