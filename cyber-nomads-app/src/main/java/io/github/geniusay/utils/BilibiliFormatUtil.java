package io.github.geniusay.utils;

import java.util.ArrayList;
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

    public static List<String> parseKeywords(String keywordSearchText) {
        Pattern pattern = Pattern.compile("【(.*?)】");
        Matcher matcher = pattern.matcher(keywordSearchText);

        List<String> keywords = new ArrayList<>();
        while (matcher.find()) {
            String keyword = matcher.group(1).trim();
            if (!keyword.isEmpty()) {
                keywords.add(keyword);
            }
        }
        return keywords.stream().distinct().collect(Collectors.toList());
    }
}
