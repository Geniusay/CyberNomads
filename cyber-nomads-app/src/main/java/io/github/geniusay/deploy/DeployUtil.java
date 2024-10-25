package io.github.geniusay.deploy;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class DeployUtil {

    private static boolean autoPush = false;

    private static boolean isFront = false;

    private static final String PORT = "9000";

    private static String commit = "";
    static Scanner scanner = new Scanner(System.in);

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String currentTime = LocalDateTime.now().format(dateTimeFormatter);

    public static void main(String[] args) {
        deployInit();
        deployInfoPrint();
        String commitMsg = isFront?commitFrontMsg(commit):commitMsg(commit,PORT,PORT,"prod","1.0.0");
        String logMessage = String.format("%s commit: %s", currentTime, commitMsg);
        writeLogToFile(logMessage);
        gitCommit(commitMsg);
    }

    private static void deployInit(){
        System.out.print("请输入commit:");
        String temp;
        commit = !StringUtils.isBlank(temp=scanner.nextLine())?String.format("上线时间:%s", currentTime):temp;
        System.out.print("是否自动push(输入任意字符为Y):");
        autoPush = !StringUtils.isBlank(scanner.nextLine());
        System.out.print("是否为前端部署(输入任意字符为Y):");
        isFront = !StringUtils.isBlank(scanner.nextLine());
    }

    private static void deployInfoPrint(){
        System.out.println("=============================");
        System.out.println("当前commit："+commit);
        System.out.println("是否自动push："+autoPush);
        System.out.println("部署服务："+(isFront?"前端":"后端"));
        System.out.println("=============================");
    }
    static final String COMMIT_FORMAT = "<Auto> %s -v:%s -rp:%s -de:<-e ACTIVE=%s -e SERVER_PORT=%S>";
    static final String FRONT_COMMIT_FORMAT = "<Auto-Frontend> %s";

    private static String commitMsg(String msg, String port, String runPort, String active, String version){
        return String.format(COMMIT_FORMAT, msg, version, runPort, active, port);
    }

    private static String commitFrontMsg(String msg){
        return String.format(FRONT_COMMIT_FORMAT, msg);
    }
    private static void writeLogToFile(String logMessage) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("E:\\Project\\GeniusWorld\\genius-blog-backend\\prod_deploy.log", true))) {
            writer.write(logMessage);
            writer.newLine();
            System.out.println("Log written to prod_deploy.log");
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }

    private static void gitCommit(String commitMessage) {
        try {
            // Stage all changes
            executeCommand("git add .");
            // Commit with the provided message
            executeCommand("git commit -m \"" + commitMessage + "\"");
            if(autoPush){
                executeCommand("git push origin master");
            }
            System.out.println("Git commit executed with message: " + commitMessage);
        } catch (IOException | InterruptedException e) {
            System.err.println("Error during Git commit: " + e.getMessage());
        }
    }

    private static void executeCommand(String command) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
        processBuilder.redirectErrorStream(true); // 合并标准错误和标准输出
        Process process = processBuilder.start();

        // 等待命令执行完成
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            System.err.println("Command failed with exit code: " + exitCode);
        }
    }
}
