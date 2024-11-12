package io.github.geniusay.service.Impl;

import com.alibaba.fastjson.JSON;
import io.github.geniusay.pojo.DTO.LoginDTO;
import io.github.geniusay.pojo.DTO.VerityDTO;
import io.github.geniusay.pojo.VO.RobotVO;
import io.github.geniusay.service.UserService;
import io.github.geniusay.strategy.login.AbstractLoginStrategy;
import io.github.geniusay.strategy.login.LoginStrategyFactory;
import io.github.geniusay.util.CacheUtils;
import io.github.geniusay.util.HTTPUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.geniusay.common.Constant.*;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/11 14:16
 */
@Service
public class IUserServiceImpl implements UserService {

    @Resource
    AbstractLoginStrategy loginStrategy;
    @Override
    public Boolean login(LoginDTO loginDTO) {
        return loginStrategy.login(loginDTO);
    }

    @Override
    public Object queryRobots() {
        String key = CacheUtils.key;
        if(StringUtils.isBlank(key)){
            throw new RuntimeException("令牌不能为空");
        }
        Map<String, String> map = Map.of("machine-token", key);
        Object params = HTTPUtils.getWithNullParams(HTTP + LOCAL_TARGET_PATH + QUERY_USER_ROBOT, map);
        if (params instanceof List<?>) {
            List<?> paramList = (List<?>) params;
            for (Object obj : paramList) {
                if (obj instanceof RobotVO) {
                    RobotVO robot = (RobotVO) obj;
                    CacheUtils.robots.add(robot.getId());
                }
            }
        }
        return params;
    }

    @Override
    public void saveKey(String scriptKey) {
        CacheUtils.key = scriptKey.split(":")[1];
    }

    @Override
    public void removeMachineCode() {
        HTTPUtils.postWithParams(HTTP+LOCAL_TARGET_PATH+REMOVE_CODE,Map.of("machine-token",CacheUtils.key),CacheUtils.key);
        CacheUtils.key = null;
    }

}
