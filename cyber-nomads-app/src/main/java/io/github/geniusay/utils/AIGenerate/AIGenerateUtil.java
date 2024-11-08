package io.github.geniusay.utils.AIGenerate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Value;
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

    public String textGenerateAndReturnContent(String resource,Integer num,String slogan){
        String content = AIGenerate.sendAndReturnString(resource, apiKey, num);
        content+="\n"+ (StringUtil.isNullOrEmpty(slogan)?"":slogan);
        return content;
    }
    private static Integer parseToken(String res){
        JSONObject jsonObject = JSONObject.parseObject(res);
        JSONArray choices = jsonObject.getJSONArray("choices");
        JSONObject message = choices.getJSONObject(0).getJSONObject("message");
        String content = message.getString("content");
        JSONObject usage = jsonObject.getJSONObject("usage");
        return usage.getIntValue("total_tokens");
    }

}
