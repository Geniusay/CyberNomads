package io.github.geniusay.utils;

import io.github.geniusay.pojo.DTO.WbiSignatureResult;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class WbiSignatureUtil {

    private static final int[] mixinKeyEncTab = new int[] {
            46, 47, 18, 2, 53, 8, 23, 32, 15, 50, 10, 31, 58, 3,
            45, 35, 27, 43, 5, 49, 33, 9, 42, 19, 29, 28, 14, 39,
            12, 38, 41, 13, 37, 48, 7, 16, 24, 55, 40, 61, 26, 17, 0, 1,
            60, 51, 30, 4, 22, 25, 54, 21, 56, 59, 6, 63, 57, 62, 11, 36, 20, 34, 44, 52
    };

    private static final char[] hexDigits = "0123456789abcdef".toCharArray();

    private static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            char[] result = new char[messageDigest.length * 2];
            for (int i = 0; i < messageDigest.length; i++) {
                result[i * 2] = hexDigits[(messageDigest[i] >> 4) & 0xF];
                result[i * 2 + 1] = hexDigits[messageDigest[i] & 0xF];
            }
            return new String(result);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }

    private static String getMixinKey(String imgKey, String subKey) {
        String s = imgKey + subKey;
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            key.append(s.charAt(mixinKeyEncTab[i]));
        }
        return key.toString();
    }

    private static String encodeURIComponent(Object value) {
        return URLEncoder.encode(value.toString(), StandardCharsets.UTF_8).replace("+", "%20");
    }

    public static WbiSignatureResult generateSignature(Map<String, Object> params, String imgKey, String subKey) {
        String mixinKey = getMixinKey(imgKey, subKey);

        long wts = System.currentTimeMillis() / 1000;
        params.put("wts", wts);

        TreeMap<String, Object> sortedParams = new TreeMap<>(params);

        String paramString = sortedParams.entrySet().stream()
                .map(entry -> String.format("%s=%s", entry.getKey(), encodeURIComponent(entry.getValue())))
                .collect(Collectors.joining("&"));
        String stringToSign = paramString + mixinKey;
        String wRid = md5(stringToSign);
        return new WbiSignatureResult(wts, wRid);
    }
}