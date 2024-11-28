package io.github.geniusay.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/11 19:33
 */
@Getter
@AllArgsConstructor
public enum Platform {

    BILIBILI("bilibili",1),
    DOUYIN("douyin",2);

    private final String platform;
    private final Integer code;

    public static int convertStringToCode(String platform){
        for(Platform plat:Platform.values()){
            if (plat.getPlatform().equalsIgnoreCase(platform)) {
                return plat.getCode();
            }
        }
        throw new RuntimeException("不支持的平台类型!");
    }

}
