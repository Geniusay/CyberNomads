package io.github.geniusay.crawler.test.bilibili;

import io.github.geniusay.crawler.api.bilibili.BilibiliBarrageApi;
import io.github.geniusay.crawler.api.bilibili.BilibiliVideoApi;
import io.github.geniusay.crawler.po.bilibili.Barrage;
import io.github.geniusay.crawler.po.bilibili.VideoDetail;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import org.junit.Test;

import java.io.*;
import java.util.*;

import static io.github.geniusay.crawler.BCookie.cookie;
import static io.github.geniusay.crawler.test.bilibili.TestVideoAPI.imgKey;
import static io.github.geniusay.crawler.test.bilibili.TestVideoAPI.subKey;

public class TestBarrageAPI {

    // 输入视频的bvid
    private static final String SOURCE_BVID = "BV11w41177WY";  // 源视频的bvid
    private static final String TARGET_BVID = "BV1H7UZYtEH3";  // 目标视频的bvid

    // 保存弹幕列表的文件路径
    private static final String BARRAGE_FILE_PATH = "D:\\" + SOURCE_BVID + ".txt";

    // 记录发送进度的文件路径
    private static final String PROGRESS_FILE = "D:\\progress.txt";

    // 基础发送间隔（秒），实际间隔会在此基础上上下浮动20%
    private static final int BASE_SEND_INTERVAL = 30;

    // 时间偏移量，单位秒，表示弹幕发送时间向后偏移的时间
    private static final int TIME_OFFSET = 12;

    /**
     * 从源视频爬取弹幕并保存到文件中
     */
    @Test
    public void saveBarrageToFile() throws Exception {
        // 获取源视频视频详情
        ApiResponse<VideoDetail> videoResponse = BilibiliVideoApi.getVideoDetailById(cookie, SOURCE_BVID);
        VideoDetail videoDetail = videoResponse.getData();
        if (!videoResponse.isSuccess() || videoDetail == null) {
            System.out.println("获取源视频信息失败：" + videoResponse.getMsg());
            return;
        }

        // 获取源视频的cid
        long sourceCid = videoDetail.getData().getPages().get(0).getCid();

        // 获取源视频的弹幕列表
        ApiResponse<List<Barrage>> response = BilibiliBarrageApi.getRealTimeBarrageByCid(cookie, String.valueOf(sourceCid));

        if (response.isSuccess()) {
            List<Barrage> barrageList = response.getData();
            // 保存弹幕列表到文件
            saveBarrageList(BARRAGE_FILE_PATH, barrageList);
        } else {
            System.out.println("获取弹幕失败：" + response.getMsg());
        }
    }

    /**
     * 从文件中读取弹幕并发送到目标视频
     */
    @Test
    public void sendBarrageFromFile() throws Exception {
        // 获取目标视频视频详情
        ApiResponse<VideoDetail> videoResponse = BilibiliVideoApi.getVideoDetailById(cookie, TARGET_BVID);
        VideoDetail videoDetail = videoResponse.getData();
        if (!videoResponse.isSuccess() || videoDetail == null) {
            System.out.println("获取目标视频信息失败：" + videoResponse.getMsg());
            return;
        }

        // 获取目标视频的aid和cid
        long targetAid = videoDetail.getData().getAid();
        long targetCid = videoDetail.getData().getPages().get(0).getCid();

        // 加载已发送弹幕的进度
        Set<Long> sentDmids = loadProgress(PROGRESS_FILE);

        // 从文件中加载弹幕列表
        List<Barrage> barrageList = loadBarrageList(BARRAGE_FILE_PATH);

        // 遍历弹幕列表并将其发送到目标视频
        for (Barrage barrage : barrageList) {
            // 如果弹幕已经发送过，跳过
            if (sentDmids.contains(barrage.getDmid())) {
                continue;
            }

            String message = barrage.getContent();
            // 加上偏移时间（单位：毫秒）
            int progress = (int) ((barrage.getTime() + TIME_OFFSET) * 1000);

            // 将弹幕时间转换为分钟和秒
            int totalSeconds = (int) (barrage.getTime() + TIME_OFFSET);
            int minutes = totalSeconds / 60;
            int seconds = totalSeconds % 60;

            int color = 16777215;  // 弹幕颜色，白色
            int fontsize = 25;  // 弹幕字体大小，标准
            int mode = barrage.getType();  // 弹幕类型

            // 发送弹幕到目标视频
            ApiResponse<Boolean> sendResponse = BilibiliBarrageApi.sendBarrage(cookie, String.valueOf(targetAid), String.valueOf(targetCid), message, progress, color, fontsize, mode, imgKey, subKey);

            if (sendResponse.isSuccess()) {
                System.out.printf("弹幕发送成功: [%02d:%02d] %s%n", minutes, seconds, message);
                // 将已发送的弹幕dmid记录到文件
                sentDmids.add(barrage.getDmid());
                saveProgress(PROGRESS_FILE, sentDmids);
            } else {
                System.out.printf("弹幕发送失败: [%02d:%02d] %s，原因：%s%n", minutes, seconds, message, sendResponse.getMsg());
            }

            // 延时一段时间，避免触发风控
            int randomInterval = getRandomInterval(BASE_SEND_INTERVAL);
            System.out.println("等待 " + randomInterval + " 秒后发送下一条弹幕...");
            Thread.sleep(randomInterval * 1000L);  // 转换为毫秒
        }
    }

    /**
     * 获取一个随机的发送间隔时间，基于基础间隔时间上下浮动20%
     */
    private int getRandomInterval(int baseInterval) {
        // 计算上下浮动的范围
        int minInterval = (int) (baseInterval * 0.8);
        int maxInterval = (int) (baseInterval * 1.2);

        // 生成[minInterval, maxInterval]范围内的随机数
        return new Random().nextInt(maxInterval - minInterval + 1) + minInterval;
    }

    /**
     * 将弹幕列表逐行写入到文本文件
     */
    public void saveBarrageList(String filePath, List<Barrage> barrageList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Barrage barrage : barrageList) {
                // 写入每个Barrage对象的字段，用逗号分隔
                writer.write(barrage.getDmid() + "," + barrage.getContent() + "," + barrage.getTime() + "," + barrage.getSendTime() + "," + barrage.getType() + "," + barrage.getSenderHash());
                writer.newLine();  // 换行
            }
            System.out.println("弹幕列表已成功保存到文件：" + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从文本文件逐行读取弹幕列表
     */
    public List<Barrage> loadBarrageList(String filePath) {
        List<Barrage> barrageList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // 解析每一行，按逗号分隔
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    long dmid = Long.parseLong(parts[0]);
                    String content = parts[1];
                    float time = Float.parseFloat(parts[2]);
                    long sendTime = Long.parseLong(parts[3]);
                    int type = Integer.parseInt(parts[4]);
                    String senderHash = parts[5];

                    // 创建Barrage对象并加入列表
                    Barrage barrage = new Barrage(dmid, content, time, sendTime, type, senderHash);
                    barrageList.add(barrage);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return barrageList;
    }

    /**
     * 加载已发送的弹幕dmid列表
     */
    private Set<Long> loadProgress(String filePath) {
        Set<Long> sentDmids = new HashSet<>();
        File file = new File(filePath);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sentDmids.add(Long.parseLong(line));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sentDmids;
    }

    /**
     * 将发送进度保存到文件中
     */
    private void saveProgress(String filePath, Set<Long> sentDmids) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Long dmid : sentDmids) {
                writer.write(dmid.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}