package io.github.geniusay.service.Impl;

import com.alibaba.fastjson.JSON;
import io.github.geniusay.pojo.DTO.DriverPathDTO;
import io.github.geniusay.pojo.DTO.LoginDTO;
import io.github.geniusay.pojo.DTO.VerityDTO;
import io.github.geniusay.pojo.VO.RobotVO;
import io.github.geniusay.service.UserService;
import io.github.geniusay.strategy.login.AbstractLoginStrategy;
import io.github.geniusay.strategy.login.LoginStrategyFactory;
import io.github.geniusay.util.CacheUtils;
import io.github.geniusay.util.HTTPUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
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
    @Value("${communication.url}")
    private String url;

    @Override
    public Boolean login(LoginDTO loginDTO) {
        return loginStrategy.login(loginDTO);
    }

    @Override
    public Object queryRobots() {
        String key = CacheUtils.key;
        if(StringUtils.isBlank(key)){
            throw new RuntimeException("令牌不合法，请检查");
        }
        Map<String, String> map = Map.of("machine-token", key);
        Object params = HTTPUtils.getWithNullParams(url + QUERY_USER_ROBOT, map);
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
        HTTPUtils.postWithParams(url+REMOVE_CODE,Map.of("machine-token",CacheUtils.key),CacheUtils.key);
        CacheUtils.key = null;
    }

    @Override
    public Boolean verityCode(String code) {
        if (HTTPUtils.postWithParams(url+VERITY_CODE, Map.of("machine-token",code),"")==Boolean.TRUE) {
            CacheUtils.key = code;
            return true;
        }
        return false;
    }

    @Override
    public Boolean verityPath(DriverPathDTO pathDTO) {
        String browserPath = pathDTO.getBrowserPath();
        String driverPath = pathDTO.getDriverPath();
        if(StringUtils.isBlank(browserPath)||StringUtils.isBlank(driverPath)){
            throw new RuntimeException("路径不能为空");
        }
        String key = CacheUtils.key;
        if(StringUtils.isBlank(key)){
            throw new RuntimeException("令牌不合法，请检查");
        }
        if(!isPathExist(browserPath)||!isPathExist(driverPath)){
            throw new RuntimeException("无法找到对应文件，请检查对应文件是否存在或路径是否正确");
        }
        String jsonString = JSON.toJSONString(pathDTO);
        String directory = System.getProperty("user.dir");
        String filePath = directory + File.separator + "path.txt";
        File file = new File(filePath);
        try (FileWriter fileWriter = new FileWriter(file)) {
            if (!file.exists()) {
                file.createNewFile();
            }
            fileWriter.write(jsonString);
            System.out.println("JSON 数据已写入文件: " + filePath);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public DriverPathDTO queryPathExist(){
        String key = CacheUtils.key;
        if(StringUtils.isBlank(key)){
            throw new RuntimeException("令牌不合法，请检查");
        }
        String directory = System.getProperty("user.dir");
        String filePath = directory + File.separator + "path.txt";
        if(isPathExist(filePath)){
            StringBuilder content = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");  // 拼接每一行
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return JSON.parseObject(content.toString(),DriverPathDTO.class);
        }
        return null;
    }

    private static boolean isPathExist(String path) {
        File file = new File(path);
        return file.exists();
    }
}
