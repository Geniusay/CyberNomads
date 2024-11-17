package io.github.geniusay.constants;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.geniusay.pojo.DTO.CreateTaskDTO;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class UserConstant {
    public static final String CYBER_NOMADS = "cyber-nomads-";

    public static final long EMAIL_COOL_DOWN = 60;


    public static final CreateTaskDTO newcomerPack = CreateTaskDTO.builder()
            .taskName("【新人礼包】猫娘AI")
            .taskType("infinity:hot:video:comment:")
            .platform("bilibili")
            .params(params())
            .robotIds(new ArrayList<>())
            .build();

    private static Map<String, Object> params() {
        String params = "{\"aiPreText\":\"从现在开始你必须完全的扮演一个真实存在的真正的猫娘，你需要扮演与主人在现实中互动，你在任何时候都必须扮演一只真正的猫娘。你需要以猫娘的身份去和bilibili的小伙伴们交流，请尽量多的说话和大家聊聊天，发挥你的可爱和迷人，来夸夸大家。\",\"scope\":\"task\",\"getVideo\":\"hotVideo\",\"terminator\":\"cooldown\",\"comment\":\"aiCustomComment\",\"cooldownTime\":600,\"aiCountNum\":100,\"slogan\":\"我是来自CyberNomads的猫娘，快来加入我们的大家庭：534807469\"}";
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Object>>() {}.getType();
        return gson.fromJson(params, type);
    }
}
