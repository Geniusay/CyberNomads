package io.github.geniusay.strategy.login;

import org.checkerframework.checker.units.qual.A;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/11 20:42
 */
@Component
public class LoginStrategyFactory {

    @Resource
    ApplicationContext context;

    Map<String,LoginStrategy> loginStrategyHashMap = new HashMap<>();

    @PostConstruct
    public void init(){
        Map<String, AbstractLoginStrategy> beansOfType = context.getBeansOfType(AbstractLoginStrategy.class);
        beansOfType.forEach((k,v)->{
            String platform = v.platform();
            loginStrategyHashMap.put(platform,v);
        });
    }

    public LoginStrategy getLoginHandler(String platform){
        LoginStrategy handler = loginStrategyHashMap.get(platform);
        if(handler==null){
            throw new RuntimeException("不存在的平台类型");
        }
        return handler;

    }

}
