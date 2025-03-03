package io.github.geniusay.core;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JavaFilePrinter {
    public static void main(String[] args) {
        Path rootPath = Paths.get("D:\\CyberNomads\\cyber-nomads-app\\src\\main\\java\\io\\github\\geniusay\\core\\ai");  // 替换为实际路径
        try {
            printJavaFiles(rootPath);
        } catch (IOException e) {
            System.err.println(" 处理失败: " + e.getMessage()); 
        }
    }

    private static void printJavaFiles(Path path) throws IOException {
        Files.walk(path) 
            .filter(Files::isRegularFile)
            .filter(p -> p.toString().endsWith(".java")) 
            .forEach(p -> {
                try {
                    System.out.println("\n\n=========  " + p + " =========\n");
                    Files.lines(p,  StandardCharsets.UTF_8).forEach(System.out::println); 
                } catch (IOException e) {
                    System.err.println(" 读取失败: " + p);
                }
            });
    }
}