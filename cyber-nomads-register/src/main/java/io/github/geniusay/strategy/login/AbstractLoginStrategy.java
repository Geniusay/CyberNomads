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
        }
        try {
            String cookie = loginStrategyHashMap.get(loginDTO.getPlatform()).execute();
            if (!StringUtils.isBlank(cookie)) {
                LoginMachineDTO robotDTO = LoginMachineDTO.builder().username(loginDTO.getUsername()).cookie(cookie).platform(loginDTO.getPlatform()).build();
                return HTTPUtils.postWithParams(url + INSERT_ROBOT, Map.of("machine-token", key), JSON.toJSONString(robotDTO))==Boolean.TRUE;
            }else{
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }

    public abstract String execute();
}
