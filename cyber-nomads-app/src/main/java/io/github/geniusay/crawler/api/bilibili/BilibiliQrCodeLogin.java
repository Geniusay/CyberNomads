package io.github.geniusay.crawler.api.bilibili;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import okhttp3.*;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.ServerException;
import java.util.Base64;
import java.util.List;

import static io.github.geniusay.crawler.api.bilibili.BiliTicket.getBiliTicket;

public class BilibiliQrCodeLogin {

    public static final String QR_CODE_GENERATE_URL = "https://passport.bilibili.com/x/passport-login/web/qrcode/generate?source=main-fe-header";
    public static final String QR_CODE_POLL_URL = "https://passport.bilibili.com/x/passport-login/web/qrcode/poll";
    public static final String SPI_URL = "https://api.bilibili.com/x/frontend/finger/spi";
    public static final String BILIBILI_URL = "https://www.bilibili.com/";
    public static final OkHttpClient CLIENT = new OkHttpClient();
    public static final String COOKIE_FILE = "bz-cookie.txt";
    public static String qrcodeKey;

    // 自定义请求头
    public static final Headers HEADERS = new Headers.Builder()
            .add("accept", "application/json, text/plain, */*")
            .add("accept-language", "zh-CN,zh;q=0.9,en;q=0.8")
            .add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36 Edg/116.0.1938.81")
            .build();

    @Test
    public void test() throws IOException {
        String s = checkQrCodeStatusOnce("58845fe72da80f66e3eebddd0407d103");
        System.out.println(s);
    }

    public static void main(String[] args) {
        try {
            // Step 1: 生成二维码并保存到本地
            System.out.println("正在生成二维码...");
            String qrCodeBase64 = generateQrCode();
            System.out.println("二维码生成成功，Base64 数据已生成！");
            System.out.println("二维码图片已保存为 qrcode.png，请在当前目录下查看并使用 B 站客户端扫码。");

            // Step 2: 开始轮询二维码状态
            System.out.println("开始轮询二维码状态...");

            System.out.println(qrcodeKey);
            String cookies = pollQrCodeStatus();

            // Step 3: 登录成功后获取 buvid3 / buvid4 / b_nut
            if (!cookies.isEmpty()) {
                System.out.println("登录成功，正在获取 buvid3 / buvid4 / b_nut...");
                JsonObject buvids = fetchBuvids(cookies);
                String buvid3 = buvids.get("b_3").getAsString();
                String buvid4 = buvids.get("b_4").getAsString();
                String bNut = fetchBNut(cookies);

                // Step 4: 从 Cookie 中提取 bili_jct
                String biliJct = extractBiliJct(cookies);
                if (biliJct == null) {
                    throw new RuntimeException("bili_jct 未找到，无法生成 bili_ticket！");
                }

                // Step 5: 调用 getBiliTicket 接口获取 bili_ticket
                System.out.println("正在生成 bili_ticket...");
                String biliTicketJson = getBiliTicket(biliJct);

                // 从返回的 JSON 中解析出 bili_ticket
                String biliTicket = parseBiliTicket(biliTicketJson);
                System.out.println("bili_ticket 获取成功：" + biliTicket);

                // 拼接完整 Cookie
                cookies += " buvid3=" + buvid3 + "; buvid4=" + buvid4 + "; b_nut=" + bNut + "; bili_ticket=" + biliTicket + ";";
                System.out.println("完整 Cookie 信息如下：");
                System.out.println(cookies);

                // 保存完整 Cookie 到本地文件
                saveCookies(cookies);
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
     * 从 JSON 响应中解析出 bili_ticket
     *
     * @param biliTicketJson getBiliTicket 返回的 JSON 字符串
     * @return bili_ticket
     */
    public static String parseBiliTicket(String biliTicketJson) {
        JsonObject jsonObject = JsonParser.parseString(biliTicketJson).getAsJsonObject();
        if (jsonObject.get("code").getAsInt() == 0) {
            return jsonObject.getAsJsonObject("data").get("ticket").getAsString();
        } else {
            throw new RuntimeException("解析 bili_ticket 失败，服务器返回：" + jsonObject.get("message").getAsString());
        }
    }

    public static String extractBiliJct(String cookies) {
        for (String cookie : cookies.split(";")) {
            if (cookie.trim().startsWith("bili_jct=")) {
                return cookie.split("=")[1].trim();
            }
        }
        return null;
    }


    /**
     * 调用接口获取 buvid3 和 buvid4
     *
     * @param cookies 登录成功后的 Cookie
     * @return 包含 buvid3 和 buvid4 的 JSON 对象
     * @throws IOException 网络请求异常
     */
    public static JsonObject fetchBuvids(String cookies) throws IOException {
        Request request = new Request.Builder()
                .url(SPI_URL)
                .headers(HEADERS)
                .addHeader("Cookie", cookies)
                .get()
                .build();

        try (Response response = CLIENT.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
                if (jsonObject.get("code").getAsInt() == 0) {
                    return jsonObject.getAsJsonObject("data");
                } else {
                    throw new IOException("获取 buvid3 和 buvid4 失败，服务器返回：" + jsonObject.get("message").getAsString());
                }
            } else {
                throw new IOException("获取 buvid3 和 buvid4 失败，HTTP 状态码：" + response.code());
            }
        }
    }

    /**
     * 从响应头中获取 b_nut
     *
     * @param cookies 登录成功后的 Cookie
     * @return b_nut
     * @throws IOException 网络请求异常
     */
    public static String fetchBNut(String cookies) throws IOException {
        Request request = new Request.Builder()
                .url(BILIBILI_URL)
                .headers(HEADERS)
                .addHeader("Cookie", cookies)
                .get()
                .build();

        try (Response response = CLIENT.newCall(request).execute()) {
            if (response.isSuccessful()) {
                List<String> setCookies = response.headers("Set-Cookie");
                for (String setCookie : setCookies) {
                    if (setCookie.startsWith("b_nut=")) {
                        return setCookie.split("=")[1].split(";")[0];
                    }
                }
                throw new IOException("b_nut 未找到！");
            } else {
                throw new IOException("获取 b_nut 失败，HTTP 状态码：" + response.code());
            }
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
                        throw new ServerException("二维码未扫码，请稍后重试！");
                    case 86090: // 已扫码未确认
                        throw new ServerException("二维码已扫码，但未确认登录！");
                    case 86038: // 二维码失效
                        throw new ServerException("二维码已失效，请重新生成！");
                    case 0: // 登录成功
                        List<String> cookies = response.headers("Set-Cookie");
                        return extractCookies(cookies);
                    default:
                        throw new ServerException("未知状态，状态码：" + code);
                }
            } else {
                throw new ServerException("请求失败，HTTP 状态码：" + response.code());
            }
        }
    }

    /**
     * 轮询二维码状态
     *
     * @return 登录成功后的 Cookie 信息
     */
    private static String pollQrCodeStatus() {
        final StringBuilder cookieResult = new StringBuilder();
        boolean isPolling = true;

        while (isPolling) {
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
                                isPolling = false;
                                break;
                            case 0: // 登录成功
                                System.out.println("登录成功！");
                                List<String> cookies = response.headers("Set-Cookie");
                                cookieResult.append(extractCookies(cookies));
                                isPolling = false;
                                break;
                            default:
                                System.out.println("未知状态，状态码：" + code);
                                isPolling = false;
                                break;
                        }
                    } else {
                        System.err.println("请求失败，HTTP 状态码：" + response.code());
                        isPolling = false;
                    }
                }

                // 每次轮询后等待 2 秒
                Thread.sleep(2000);

            } catch (Exception e) {
                System.err.println("轮询失败：" + e.getMessage());
                isPolling = false;
            }
        }

        return cookieResult.toString();
    }

    /**
     * 提取并拼接完整的 Cookie 信息
     *
     * @param cookies 响应头中的 Set-Cookie 列表
     * @return 拼接后的完整 Cookie 字符串
     */
    public static String extractCookies(List<String> cookies) {
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