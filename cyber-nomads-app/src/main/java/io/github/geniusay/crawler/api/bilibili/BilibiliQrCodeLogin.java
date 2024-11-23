package io.github.geniusay.crawler.api.bilibili;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import okhttp3.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BilibiliQrCodeLogin {

    private static final String QR_CODE_GENERATE_URL = "https://passport.bilibili.com/x/passport-login/web/qrcode/generate?source=main-fe-header";
    private static final String QR_CODE_POLL_URL = "https://passport.bilibili.com/x/passport-login/web/qrcode/poll";

    private static final OkHttpClient CLIENT = new OkHttpClient();
    private static String qrcodeKey;

    // 保存二维码图片的路径
    private static final String QR_CODE_IMAGE_PATH = "D:\\qrcode.png";

    public static void main(String[] args) {
        try {
            // Step 1: 生成二维码
            String qrCodeUrl = generateQrCode();
            if (qrCodeUrl != null) {
                System.out.println("请扫描以下二维码登录：");
                generateQrCodeImage(qrCodeUrl, QR_CODE_IMAGE_PATH);
                System.out.println("二维码已保存为 qrcode.png，请使用 B 站客户端扫码登录。");
            }

            // Step 2: 轮询二维码状态
            pollQrCodeStatus();

        } catch (Exception e) {
            System.err.println("程序运行出错：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 生成二维码 URL 和 key
     *
     * @return 二维码 URL
     * @throws IOException 网络请求异常
     */
    private static String generateQrCode() throws IOException {
        Request request = new Request.Builder()
                .url(QR_CODE_GENERATE_URL)
                .get()
                .build();

        try (Response response = CLIENT.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                System.out.println("二维码生成响应：" + responseBody);

                // 使用 Gson 解析 JSON 获取二维码 URL 和 key
                JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
                JsonObject data = jsonObject.getAsJsonObject("data");
                qrcodeKey = data.get("qrcode_key").getAsString();
                return data.get("url").getAsString().replace("\\u0026", "&");
            } else {
                throw new IOException("二维码生成失败，HTTP 状态码：" + response.code());
            }
        }
    }

    /**
     * 开始轮询二维码状态
     */
    private static void pollQrCodeStatus() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    String pollUrl = QR_CODE_POLL_URL + "?qrcode_key=" + qrcodeKey + "&source=main-fe-header";
                    Request request = new Request.Builder()
                            .url(pollUrl)
                            .get()
                            .build();

                    try (Response response = CLIENT.newCall(request).execute()) {
                        if (response.isSuccessful() && response.body() != null) {
                            String responseBody = response.body().string();
                            System.out.println("轮询响应：" + responseBody);

                            // 使用 Gson 解析 JSON 获取状态码
                            JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
                            JsonObject data = jsonObject.getAsJsonObject("data");
                            int code = data.get("code").getAsInt();

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
                                    String completeCookie = extractCookies(response.headers("Set-Cookie"));
                                    System.out.println("最终拼接的完整 Cookie: " + completeCookie);
                                    timer.cancel();
                                    break;
                                default:
                                    System.out.println("未知状态，响应：" + responseBody);
                                    timer.cancel();
                                    break;
                            }
                        } else {
                            System.err.println("轮询失败，HTTP 状态码：" + response.code());
                        }
                    }
                } catch (Exception e) {
                    System.err.println("轮询二维码状态失败：" + e.getMessage());
                    e.printStackTrace();
                }
            }
        };

        // 每 2 秒轮询一次
        timer.schedule(task, 0, 2000);
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
                cookieBuilder.append(parts[0]).append("; "); // 拼接键值对
            }
        }
        return cookieBuilder.toString().trim();
    }

    /**
     * 生成二维码图片
     *
     * @param qrCodeUrl 二维码内容
     * @param filePath  保存路径
     */
    private static void generateQrCodeImage(String qrCodeUrl, String filePath) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeUrl, BarcodeFormat.QR_CODE, 300, 300);
            Path path = Paths.get(filePath);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
            System.out.println("二维码已生成并保存到：" + path.toAbsolutePath());
        } catch (WriterException | IOException e) {
            System.err.println("生成二维码图片失败：" + e.getMessage());
        }
    }
}