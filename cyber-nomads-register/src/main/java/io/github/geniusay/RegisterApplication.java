package io.github.geniusay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SpringBootApplication
@EnableAsync
public class RegisterApplication {

    public static void main(String[] args) throws IOException {
        killProcessOnPort(9989);
        SpringApplication.run(RegisterApplication.class, args);
    }
    private static void killProcessOnPort(int port) {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            String command = null;

            if (os.contains("win")) {
                // Windows系统命令
                command = "cmd /c netstat -ano | findstr :" + port;
            } else {
                // Linux/Unix/MacOS系统命令
                command = "lsof -i :" + port;
            }

            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            String pid = null;

            while ((line = reader.readLine()) != null) {
                if (os.contains("win")) {
                    // Windows: 解析 PID
                    String[] tokens = line.trim().split("\\s+");
                    if (tokens.length > 4) {
                        pid = tokens[tokens.length - 1]; // PID 通常在最后一列
                    }
                } else {
                    // Linux/Unix/MacOS: 解析 PID
                    String[] tokens = line.trim().split("\\s+");
                    if (tokens.length > 1) {
                        pid = tokens[1]; // PID 通常是第二列
                    }
                }

                if (pid != null) {
                    // 杀死进程
                    String killCommand = os.contains("win") ? "taskkill /F /PID " + pid : "kill -9 " + pid;
                    Runtime.getRuntime().exec(killCommand);
                    System.out.println("Terminated process with PID: " + pid);
                }
            }
        } catch (Exception e) {
            System.err.println("Error while killing process on port " + port + ": " + e.getMessage());
        }
    }

}
