package io.github.geniusay.utils.AIGenerate;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/30 0:14
 */
@Component
public class AIGenerateUtil {

    @Resource(name="${AIGenerate.STRATEGY}")
    private BaseGenerate AIGenerate;

    @Value("${AIGenerate.API_KEY}")
    private String apiKey;

    public void textGenerate(String resource){
        AIGenerate.send(resource,apiKey);
    }

}
