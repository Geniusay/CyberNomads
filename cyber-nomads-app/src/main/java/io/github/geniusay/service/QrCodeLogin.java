package io.github.geniusay.service;

import java.io.IOException;

/**
 * 描述: 扫码登录验证接口
 * @author suifeng
 * 日期: 2024/12/13
 */
public interface QrCodeLogin {

    String getCookieByKey(String key) throws IOException;
}
