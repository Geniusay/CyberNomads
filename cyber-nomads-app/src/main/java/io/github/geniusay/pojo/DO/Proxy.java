package io.github.geniusay.pojo.DO;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@TableName("proxy")
@NoArgsConstructor
public class Proxy {
    // 评级参数：获取次数，成功次数，失败次数，延时，
    private String ip;

    private int port;
}