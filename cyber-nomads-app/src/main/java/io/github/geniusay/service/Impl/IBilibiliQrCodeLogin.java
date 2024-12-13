package io.github.geniusay.service.Impl;

import io.github.geniusay.crawler.api.bilibili.BilibiliQrCodeLogin;
import io.github.geniusay.service.QrCodeLogin;

import java.io.IOException;

/**
 * 描述: b站扫码登录验证
 * @author suifeng
 * 日期: 2024/12/13
 */
public class IBilibiliQrCodeLogin implements QrCodeLogin {

    @Override
    public String getCookieByKey(String key) throws IOException {
        return BilibiliQrCodeLogin.checkQrCodeStatusOnce(key);
    }
}
