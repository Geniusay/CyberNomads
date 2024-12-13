package io.github.geniusay.service.Impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.geniusay.core.exception.ServeException;
import io.github.geniusay.service.QrCodeLogin;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.List;

import static io.github.geniusay.crawler.api.bilibili.BilibiliQrCodeLogin.*;

/**
 * 描述: b站扫码登录验证
 * @author suifeng
 * 日期: 2024/12/13
 */
@Service
public class IBilibiliQrCodeLogin implements QrCodeLogin {

    @Override
    public String getCookieByKey(String key) throws IOException {
        return checkQrCodeStatusOnce(key);
    }

    /**
     * 单次调用二维码状态检查
     *
     * @param qrcodeKey 二维码的唯一标识符
     * @return 登录成功后的 Cookie 信息
     */
    public static String checkQrCodeStatusOnce(String qrcodeKey) throws IOException {
        String pollUrl = QR_CODE_POLL_URL + "?qrcode_key=" + qrcodeKey + "&source=main-fe-header";
        Request request = new Request.Builder()
                .url(pollUrl)
                .headers(HEADERS)
                .get()
                .build();

        try (Response response = CLIENT.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
                JsonObject data = jsonObject.getAsJsonObject("data");
                int code = data.get("code").getAsInt();

                // 根据状态码处理
                switch (code) {
                    case 86101: // 未扫码
                        throw new ServeException("二维码未扫码，请稍后重试！");
                    case 86090: // 已扫码未确认
                        throw new ServeException("二维码已扫码，但未确认登录！");
                    case 86038: // 二维码失效
                        throw new ServeException("二维码已失效，请重新生成！");
                    case 0: // 登录成功
                        List<String> cookies = response.headers("Set-Cookie");
                        return extractCookies(cookies);
                    default:
                        throw new ServeException("未知状态，状态码：" + code);
                }
            } else {
                throw new ServeException("请求失败，HTTP 状态码：" + response.code());
            }
        }
    }
}
