package io.github.geniusay.crawler.test.bilibili;

import io.github.geniusay.crawler.api.bilibili.BilibiliBarrageApi;
import io.github.geniusay.crawler.po.bilibili.Barrage;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static io.github.geniusay.crawler.BCookie.cookie;

public class TestBarrageAPI {

    @Test
    public void getBarrage() throws Exception {
        String cid = "1428089940";
        String filePath = "C:\\_data\\" + cid + ".txt";

        // 获取弹幕列表
        ApiResponse<List<Barrage>> response = BilibiliBarrageApi.getRealTimeBarrageByCid(cookie, cid);

        if (response.isSuccess()) {

            List realTimeBarrageByOid = response.getData();

            // 保存弹幕列表到文件
            saveBarrageList(filePath, realTimeBarrageByOid);

            // 逐行读取：从文件中加载弹幕列表
            List<Barrage> loadedBarrageList = loadBarrageList(filePath);

            // 打印读取后的弹幕列表
            if (loadedBarrageList != null) {
                for (Barrage barrage : loadedBarrageList) {
                    System.out.println(barrage);
                }
            }
        }

    }

    @Test
    public void barrage() throws Exception {
        String cid = "1413629350";
        String filePath = "C:\\_data\\" + cid + ".txt";
        // 逐行读取：从文件中加载弹幕列表
        List<Barrage> loadedBarrageList = loadBarrageList(filePath);
        // 打印读取后的弹幕列表
        if (loadedBarrageList != null) {
            for (Barrage barrage : loadedBarrageList) {
                float time = barrage.getTime();
                int p = (int) (time / 60);
                int b = (int) (time - p * 60);
                System.out.print(p + ":" + b + "  | (");
                System.out.println(barrage.getContent() + ")");
            }
        }
    }
//1:54  | (主打一个吓死你)

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
}
