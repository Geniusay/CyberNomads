package io.github.geniusay.strategy.login;

import com.alibaba.fastjson.JSON;
import io.github.geniusay.pojo.DTO.LoginDTO;
import io.github.geniusay.pojo.DTO.LoginMachineDTO;
import io.github.geniusay.util.CacheUtils;
import io.github.geniusay.util.HTTPUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static io.github.geniusay.common.Constant.*;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/11 21:06
 */
@Component
public abstract class AbstractLoginStrategy implements LoginStrategy{
    @Resource
    ApplicationContext context;
    @Value("${communication.url}")
    private String url;
    Map<String,AbstractLoginStrategy> loginStrategyHashMap = new HashMap<>();
    @PostConstruct
    public void init(){
        Map<String, AbstractLoginStrategy> beansOfType = context.getBeansOfType(AbstractLoginStrategy.class);
        beansOfType.forEach((k,v)->{
            String platform = v.platform();
            loginStrategyHashMap.put(platform,v);
        });
    }
    @Override
    public Boolean login(LoginDTO loginDTO) {
        String key = CacheUtils.key;
        if(StringUtils.isBlank(key)){
            throw new RuntimeException("密钥不正确或为空");
        }else{
            try {
                String cookie = loginStrategyHashMap.get(loginDTO.getPlatform()).execute();
                System.out.println(cookie);
                if (!StringUtils.isBlank(cookie)) {
                    LoginMachineDTO robotDTO = LoginMachineDTO.builder().username(loginDTO.getUsername()).cookie(cookie).platform(loginDTO.getPlatform()).build();
                    return "200".equals(HTTPUtils.convertRespToCode(HTTPUtils.postWithParams(url + INSERT_ROBOT, Map.of("machine-token", key), JSON.toJSONString(robotDTO))));
                }else {
                    throw new RuntimeException("cookie不能为空");
                }
            } catch (IOException e) {
                throw new RuntimeException("登陆失败,不正确的驱动路径或调用错误");
            }

        }
    }

    public abstract String execute();
}
