package io.github.geniusay.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/30 12:09
 */
@Getter
@AllArgsConstructor
public enum Platform {

    BILIBILI("Blibili",1),
    DOUYIN("Douyin",2);
    private String plat;
    private Integer code;

}
