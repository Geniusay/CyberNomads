package io.github.geniusay.utils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BilibiliFormatUtil {

    // 正则表达式匹配 BV 号
    private static final Pattern BV_PATTERN = Pattern.compile("BV([a-zA-Z0-9]{10})");

    /**
     * 从视频链接或 BV 号中提取 BV 号
     */
    public static String extractBvId(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("输入的视频链接或 BV 号不能为空");
        }

        // 尝试匹配 BV 号
        Matcher matcher = BV_PATTERN.matcher(input);
        if (matcher.find()) {
            return matcher.group(0);  // 返回完整的 BV 号
        }

        throw new IllegalArgumentException("无效的视频链接或 BV 号: " + input);
    }

    /**
     * 解析关键词字符串，提取以 '#' 分隔的有效关键词
     *
     * @param keywordSearchText 用户输入的关键词字符串
     * @return 解析后的关键词列表
     */
    public static List<String> parseKeywords(String keywordSearchText) {
        // 去除多余的空格，并按 '#' 分割
        String[] rawKeywords = keywordSearchText.trim().split("#");

        // 遍历分割后的关键词，过滤掉空值或无效字符
        return Arrays.stream(rawKeywords)
                .map(String::trim) // 去除每个关键词的前后空格
                .filter(keyword -> !keyword.isEmpty()) // 过滤掉空字符串
                .distinct() // 去重
                .collect(Collectors.toList()); // 收集为列表
    }
}
