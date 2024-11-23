package io.github.geniusay.crawler.api.bilibili;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import okhttp3.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BilibiliQrCodeLogin {

    private static final String QR_CODE_GENERATE_URL = "https://passport.bilibili.com/x/passport-login/web/qrcode/generate?source=main-fe-header";
    private static final String QR_CODE_POLL_URL = "https://passport.bilibili.com/x/passport-login/web/qrcode/poll";
    private static final String VERIFY_URL = "https://api.bilibili.com/x/web-interface/nav";
    private static final OkHttpClient CLIENT = new OkHttpClient();
    private static final String COOKIE_FILE = "bz-cookie.txt";
    private static String qrcodeKey;

    private static final String IP = "180.159.89.214";

    // 自定义请求头，伪造 IP 地址
    private static final Headers HEADERS = new Headers.Builder()
            .add("authority", "api.vc.bilibili.com")
            .add("accept", "application/json, text/plain, */*")
            .add("accept-language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6")
            .add("content-type", "application/x-www-form-urlencoded")
            .add("origin", "https://message.bilibili.com")
            .add("referer", "https://message.bilibili.com/")
            .add("sec-ch-ua", "\"Chromium\";v=\"116\", \"Not)A;Brand\";v=\"24\", \"Microsoft Edge\";v=\"116\"")
            .add("sec-ch-ua-mobile", "?0")
            .add("sec-ch-ua-platform", "\"Windows\"")
            .add("sec-fetch-dest", "empty")
            .add("sec-fetch-mode", "cors")
            .add("sec-fetch-site", "same-site")
            .add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36 Edg/116.0.1938.81")
            .build();

    public static void main(String[] args) {
        try {
            // Step 1: 生成二维码并保存到本地
            System.out.println("正在生成二维码...");
            String qrCodeBase64 = BilibiliQrCodeLogin.generateQrCode();
            System.out.println("二维码生成成功，Base64 数据已生成！");
            System.out.println("二维码图片已保存为 qrcode.png，请在当前目录下查看并使用 B 站客户端扫码。");

            // Step 2: 开始轮询二维码状态
            System.out.println("开始轮询二维码状态...");
            String cookies = pollQrCodeStatus();

            // Step 3: 打印并保存 Cookie
            if (!cookies.isEmpty()) {
                System.out.println("登录成功，Cookie 已获取！");
                // 保存 Cookie 到本地文件
                BilibiliQrCodeLogin.saveCookies(cookies);
                System.out.println("Cookie 已保存到本地文件！");
            } else {
                System.out.println("登录失败或二维码已过期，请重新尝试！");
            }
        } catch (Exception e) {
            System.err.println("程序运行出错：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 生成二维码并返回 Base64 编码的图片数据
     *
     * @return Base64 编码的二维码图片
     * @throws IOException 网络请求异常
     */
    public static String generateQrCode() throws IOException {
        Request request = new Request.Builder()
                .url(QR_CODE_GENERATE_URL)
                .headers(HEADERS)
                .get()
                .build();

        try (Response response = CLIENT.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
                JsonObject data = jsonObject.getAsJsonObject("data");
                qrcodeKey = data.get("qrcode_key").getAsString();
                String qrCodeUrl = data.get("url").getAsString().replace("\\u0026", "&");

                // 生成二维码图片并返回 Base64 编码
                return generateQrCodeImageBase64(qrCodeUrl);
            } else {
                throw new IOException("二维码生成失败，HTTP 状态码：" + response.code());
            }
        }
    }

    /**
     * 轮询二维码状态
     *
     * @return 登录成功后的 Cookie 信息
     */
    private static String pollQrCodeStatus() {
        Timer timer = new Timer();
        final StringBuilder cookieResult = new StringBuilder();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
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
                                    System.out.println("等待扫码...");
                                    break;
                                case 86090: // 已扫码未确认
                                    System.out.println("二维码已扫码，请在手机端确认登录...");
                                    break;
                                case 86038: // 二维码失效
                                    System.out.println("二维码已失效，请重新生成！");
                                    timer.cancel();
                                    break;
                                case 0: // 登录成功
                                    System.out.println("登录成功！");
                                    List<String> cookies = response.headers("Set-Cookie");
                                    cookieResult.append(extractCookies(cookies));
                                    timer.cancel();
                                    break;
                                default:
                                    System.out.println("未知状态，状态码：" + code);
                                    timer.cancel();
                                    break;
                            }
                        } else {
                            System.err.println("请求失败，HTTP 状态码：" + response.code());
                        }
                    }
                } catch (Exception e) {
                    System.err.println("轮询失败：" + e.getMessage());
                    timer.cancel();
                }
            }
        };

        // 每 2 秒轮询一次
        timer.schedule(task, 0, 2000);

        // 等待轮询完成
        try {
            Thread.sleep(180000); // 等待 3 分钟（二维码有效期）
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return cookieResult.toString();
    }

    /**
     * 提取并拼接完整的 Cookie 信息
     *
     * @param cookies 响应头中的 Set-Cookie 列表
     * @return 拼接后的完整 Cookie 字符串
     */
    private static String extractCookies(List<String> cookies) {
        StringBuilder cookieBuilder = new StringBuilder();

        for (String cookie : cookies) {
            // 提取每个 Cookie 的键值对部分（去掉 Path、Domain 等附加信息）
            String[] parts = cookie.split(";");
            if (parts.length > 0) {
                cookieBuilder.append(parts[0]).append("; ");
            }
        }
        return cookieBuilder.toString().trim();
    }

    /**
     * 生成二维码图片并返回 Base64 编码
     *
     * @param qrCodeUrl 二维码内容
     * @return Base64 编码的二维码图片
     * @throws IOException 生成图片失败
     */
    private static String generateQrCodeImageBase64(String qrCodeUrl) throws IOException {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeUrl, BarcodeFormat.QR_CODE, 300, 300);
            Path path = Paths.get("qrcode.png");
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

            // 读取图片并转换为 Base64
            byte[] imageBytes = java.nio.file.Files.readAllBytes(path);
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (WriterException e) {
            throw new IOException("生成二维码图片失败：" + e.getMessage());
        }
    }

    /**
     * 保存 Cookie 到本地文件
     *
     * @param cookies Cookie 信息
     */
    public static void saveCookies(String cookies) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(COOKIE_FILE))) {
            writer.write(cookies);
            System.out.println("Cookie 已保存！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}