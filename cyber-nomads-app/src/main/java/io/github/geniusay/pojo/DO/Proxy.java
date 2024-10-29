package io.github.geniusay.pojo.DO;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("proxy")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Proxy {
    private String ip;

    private int port;
}