package io.github.geniusay.util;

import com.alibaba.fastjson.JSON;
import io.github.common.web.Result;
import io.github.geniusay.pojo.DTO.DriverPathDTO;
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

    public static boolean isDownload = false;

    public static String downloadMsg = "";

    @Async
    public void downloadFile(String fileUrl, String savePath) throws IOException {
        isDownload = true;
        try {
            downloadMsg = "正在下载中....";
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
            downloadMsg = "正在解压中.....";
            String target = unzipFile(savePath);
            downloadMsg = "下载成功!";
            DriverPathDTO build = DriverPathDTO.builder().driverPath(target).browserPath(CacheUtils.path).build();
            saveFileToCurrentPath("path.txt",JSON.toJSONString(build));
        }catch(Exception e){
            downloadMsg="下载失败："+e.getMessage();
        } finally{
            isDownload = false;
        }
    }
    public String unzipFile(String zipFilePath) {
        String dirName = zipFilePath.substring(0, zipFilePath.lastIndexOf('.'));
        File destDir = new File(System.getProperty("user.dir"), dirName);

        if (!destDir.exists()) {
            destDir.mkdir();
        }
        String exeFilePath = null;

        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                File outputFile = new File(destDir, entry.getName());
                File parentDir = outputFile.getParentFile();

                if (parentDir != null && !parentDir.exists()) {
                    parentDir.mkdirs();
                }
                if (entry.isDirectory()) {
                    continue;
                }

                try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile))) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = zipInputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
                if (entry.getName().endsWith(".exe")) {
                    exeFilePath = outputFile.getAbsolutePath();
                }

                zipInputStream.closeEntry();
            }
        } catch (IOException e) {
            downloadMsg="解压失败";
            System.err.println("Error while extracting the zip file: " + e.getMessage());
            e.printStackTrace();
        }

        return exeFilePath;
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

    public static void deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            String[] files = dir.list();
            if (files != null) {
                for (String file : files) {
                    deleteDirectory(new File(dir, file));
                }
            }
        }
        dir.delete();
    }
    public void saveFileToCurrentPath(String name,String data){
        String directory = System.getProperty("user.dir");
        String filePath = directory + File.separator + name;
        File file = new File(filePath);
        try (FileWriter fileWriter = new FileWriter(file)) {
            if (!file.exists()) {
                file.createNewFile();
            }
            fileWriter.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
