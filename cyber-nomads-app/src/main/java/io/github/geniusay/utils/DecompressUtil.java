package io.github.geniusay.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class DecompressUtil {

    /**
     * 解压deflate压缩的数据流
     *
     * @param inputStream 压缩流
     * @return 解压后的字符串
     * @throws IOException
     */
    public static String decompressDeflateStream(InputStream inputStream) throws IOException {
        Inflater inflater = new Inflater(true);  // true表示使用nowrap模式，处理raw deflate
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        byte[] outputBuffer = new byte[1024];

        try {
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                inflater.setInput(buffer, 0, bytesRead);

                // 处理解压数据
                while (!inflater.needsInput()) {
                    try {
                        int count = inflater.inflate(outputBuffer);
                        outputStream.write(outputBuffer, 0, count);
                    } catch (DataFormatException e) {
                        throw new IOException("Data format exception during decompression", e);
                    }
                }
            }

            // 处理剩余的解压数据
            while (!inflater.finished()) {
                try {
                    int count = inflater.inflate(outputBuffer);
                    if (count == 0) {
                        break;  // 没有更多数据可解压，跳出循环
                    }
                    outputStream.write(outputBuffer, 0, count);
                } catch (DataFormatException e) {
                    throw new IOException("Data format exception during decompression", e);
                }
            }

        } finally {
            inflater.end();
        }

        return outputStream.toString("UTF-8");  // 根据实际情况选择合适的编码
    }
}
