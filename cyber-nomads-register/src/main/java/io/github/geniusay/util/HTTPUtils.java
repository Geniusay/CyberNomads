package io.github.geniusay.util;

import com.alibaba.fastjson.JSON;
import io.github.common.web.Result;
import io.github.geniusay.pojo.VO.RobotVO;
import okhttp3.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/11 14:18
 */
@Component
public class HTTPUtils {

    @Async
    public void downloadFile(String fileUrl, String savePath) throws IOException {
        Request request = new Request.Builder()
                .url(fileUrl)
                .build();
        OkHttpClient client = new OkHttpClient();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to download file: " + response);
            }
            InputStream inputStream = response.body().byteStream();
            try (OutputStream outputStream = new FileOutputStream(savePath)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        }
    }

    public void unzipFile(String zipFilePath) throws IOException {
        String dirName = zipFilePath.substring(0, zipFilePath.lastIndexOf('.'));
        File destDir = new File(System.getProperty("user.dir"), dirName);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        System.out.println(destDir.getPath());
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                // 处理条目名中的路径问题
                File outputFile = new File(destDir, entry.getName());
                File parentDir = outputFile.getParentFile();

                // 如果父目录不存在，则创建它
                if (parentDir != null && !parentDir.exists()) {
                    parentDir.mkdirs();
                }

                // 如果是目录条目，则跳过文件创建过程
                if (entry.isDirectory()) {
                    continue; // 直接处理下一个条目
                }

                // 创建并写入目标文件
                try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile))) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = zipInputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
                zipInputStream.closeEntry(); // 关闭当前条目
            }
        } catch (IOException e) {
            // 捕获异常并打印详细信息
            System.err.println("Error while extracting the zip file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public Response getWithNullParams(String url, Map<String,String> headers){
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request.Builder requestbuilder = new Request.Builder()
                    .url(url)
                    .get();
            for (Map.Entry<String, String> stringObjectEntry : headers.entrySet()) {
                requestbuilder.addHeader(stringObjectEntry.getKey(), stringObjectEntry.getValue());
            }
            Request request = requestbuilder.build();
            return okHttpClient.newCall(request).execute();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    public Response postWithParams(String url, Map<String,String> headers, String body) {
        try {
            MediaType mediaType = MediaType.parse("application/json");
            Request.Builder requestbuilder = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(mediaType, body));
            //增加请求头
            for (Map.Entry<String, String> stringObjectEntry : headers.entrySet()) {
                requestbuilder.addHeader(stringObjectEntry.getKey(), stringObjectEntry.getValue());
            }

            Request request = requestbuilder.build();
            OkHttpClient okHttpClient = new OkHttpClient();

            return okHttpClient.newCall(request).execute();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public Object convertRespToData(Response response) throws IOException {
        Result result = JSON.parseObject(response.body().string(),Result.class);
        return result.getData();
    }
    public String convertRespToCode(Response response) throws IOException {
        Result result = JSON.parseObject(response.body().string(),Result.class);
        return result.getCode();
    }
    public Result<?> convertRespToResult(Response response) throws IOException {
        Result result = JSON.parseObject(response.body().string(),Result.class);
        Result res = new Result();
        res.setCode(result.getCode());
        Object data = result.getData();
        res.setData(Objects.isNull(data)?null:JSON.parseArray(JSON.toJSONString(data), RobotVO.class));
        res.setMsg(result.getMsg());
        res.setTimestamp(result.getTimestamp());
        return res;
    }
}
