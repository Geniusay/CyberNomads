package io.github.geniusay.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.github.common.web.Result;
import io.github.geniusay.pojo.DTO.*;
import io.github.geniusay.pojo.VO.RobotVO;
import io.github.geniusay.service.UserService;
import io.github.geniusay.strategy.login.AbstractLoginStrategy;
import io.github.geniusay.strategy.login.LoginStrategyFactory;
import io.github.geniusay.util.CacheUtils;
import io.github.geniusay.util.HTTPUtils;
import okhttp3.Response;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    @Resource
    HTTPUtils httpUtils;

    @Value("${communication.url}")
    private String url;
    List<String> keys = new ArrayList<>();
    @PostConstruct
    public void init(){
        try {
            Process process = Runtime.getRuntime().exec(
                    "reg query HKEY_CLASSES_ROOT\\http\\shell\\open\\command");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), "GBK"));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(".exe")) {
                    String[] parts = line.split(":", 2);
                    if (parts.length > 1) {
                        String trim = parts[0].trim();
                        String browserPath = parts[1].trim();
                        browserPath = browserPath.replace("\"", "").trim();
                        int exeIndex = browserPath.toLowerCase().indexOf(".exe");
                        if (exeIndex != -1) {
                            browserPath = browserPath.substring(0, exeIndex + 4);
                        }
                        String code = String.valueOf(trim.charAt(trim.length()-1));
                        String path = code+":"+browserPath;
                        String version = queryBrowserVersion(path);
                        String fileNameWithExtension = new File(path).getName();
                        fileNameWithExtension = fileNameWithExtension.split("\\.")[0];
                        CacheUtils.version=version;
                        CacheUtils.browserName=fileNameWithExtension;
                        CacheUtils.path=path;
                    }
                }
            }
            reader.close();

            Response response = httpUtils.getWithNullParams("https://chromedriver.storage.googleapis.com/", new HashMap<>());
            String content = response.body().string();
            Pattern pattern = Pattern.compile("<Key>(.*?)</Key>");
            Matcher matcher = pattern.matcher(content);
            keys = new ArrayList<>();
            while (matcher.find()) {
                String key = matcher.group(1);
                if (key.endsWith("chromedriver_win32.zip")) {
                    keys.add(key);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Boolean login(LoginDTO loginDTO) {
        return loginStrategy.login(loginDTO);
    }

    @Override
    public Result queryRobots() {
        String key = CacheUtils.key;
        if(StringUtils.isBlank(key)){
            throw new RuntimeException("令牌不合法，请检查");
        }
        Map<String, String> map = Map.of("machine-token", key);
        try {
            return httpUtils.convertRespToResult(httpUtils.getWithNullParams(url + QUERY_USER_ROBOT, map));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveKey(String scriptKey) {
        CacheUtils.key = scriptKey.split(":")[1];
    }

    @Override
    public void removeMachineCode() {
        try {
            httpUtils.convertRespToCode(httpUtils.postWithParams(url+REMOVE_CODE,Map.of("machine-token",CacheUtils.key),CacheUtils.key));
            CacheUtils.key = null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean verityCode(String code) {
        try {
            if ("200".equals(httpUtils.convertRespToCode(httpUtils.postWithParams(url+VERITY_CODE, Map.of("machine-token",code),"")))) {
                CacheUtils.key = code;
                return true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("令牌验证错误，请校验令牌正确性");
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
        String jsonString = JSON.toJSONString(pathDTO, SerializerFeature.DisableCheckSpecialChar);

        String directory = System.getProperty("user.dir");
        String filePath = directory + File.separator + "path.txt";
        File file = new File(filePath);
        try (FileWriter fileWriter = new FileWriter(file)) {
            if (!file.exists()) {
                file.createNewFile();
            }
            fileWriter.write(jsonString);
            System.out.println(jsonString);
            System.out.println("JSON 数据已写入文件: " + filePath);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public QueryPathDTO queryPathExist(){
        String key = CacheUtils.key;
        if(StringUtils.isBlank(key)){
            throw new RuntimeException("令牌不合法，请检查");
        }
        String directory = System.getProperty("user.dir");
        String filePath = directory + File.separator + "path.txt";
        DriverPathDTO pathDTO;
        QueryPathDTO.QueryPathDTOBuilder builder = QueryPathDTO.builder();
        File driver = new File(directory+File.separator+"driver");
        if(isPathExist(filePath)){
            StringBuilder content = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            pathDTO = JSON.parseObject(content.toString(), DriverPathDTO.class);
            if(!isPathExist(pathDTO.getBrowserPath())){
                builder.errorMsg("错误的浏览器路径");
            }else if(!isPathExist(pathDTO.getDriverPath())){
                builder.errorMsg("错误的驱动路径");
            }
            builder.pathDTO(pathDTO);
            return builder.build();
        }else if(driver.exists()&&driver.isDirectory()){
            File[] files = driver.listFiles((dir, name) -> name.endsWith(".exe"));
            String browser = null;
            DriverPathDTO.DriverPathDTOBuilder driverPathDTOBuilder = DriverPathDTO.builder();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    System.out.println("Found exe: " + file.getAbsolutePath());
                    driverPathDTOBuilder.driverPath(file.getAbsolutePath());
                    browser = file.getName();
                }
            } else {
                throw new RuntimeException("找不到对应驱动文件，请重新下载");
            }
            if("edge".equals(browser)){
                driverPathDTOBuilder.browserPath(CacheUtils.path);
            }else if("chrome".equals(browser)){
                driverPathDTOBuilder.browserPath(CacheUtils.path);
            }
            builder.pathDTO(driverPathDTOBuilder.build());
        }
        return builder.build();
    }

    @Override
    public String download() {
        String name = CacheUtils.browserName;
        String version = CacheUtils.version;
        if(StringUtils.isBlank(name)||StringUtils.isBlank(version)){
            throw new RuntimeException("浏览器名称或版本不能为空");
        }
        try {
            String arch = getOsArch();
            String download;
            if("msedge".equals(name)){
                if (checkDriverExit("edgedriver_win"+arch,name+"driver.exe")) {
                    return "true";
                }
                download = "https://msedgedriver.azureedge.net/"+version+"/edgedriver_win"+arch+".zip";
                httpUtils.downloadFile(download,"edgedriver_win"+arch+".zip");
                return version;
            }else if("chrome".equals(name)){
                if(Integer.parseInt(version.split("\\.")[0])>114){
                    throw new RuntimeException("浏览器版本过高，请手动下载");
                }
                if (checkDriverExit("chromedriver_win32",name+"driver.exe")) {
                    return "true";
                }
                String nearVersion = findClosestVersionKey(keys, version);
                download = "https://chromedriver.storage.googleapis.com/"+nearVersion;
                httpUtils.downloadFile(download,"chromedriver_win32"+".zip");
                return nearVersion.split("/")[0];
            }
        } catch (IOException e) {
            throw new RuntimeException("下载失败，无法找到版本号对应的驱动，请手动下载并添加");
        }
        return null;
    }

    @Override
    public BrowserInfoDTO queryBrowser(String browser) {
        if(StringUtils.isBlank(browser)||CacheUtils.browserName.equals(browser)){
            String name = CacheUtils.browserName;
            String version = CacheUtils.version;
            String path = CacheUtils.path;
            return BrowserInfoDTO.builder().name(name).version(version).path(path).build();
        }else{
            try {
                BrowserInfoDTO.BrowserInfoDTOBuilder builder = BrowserInfoDTO.builder();
                String command;
                if ("chrome".equals(browser)) {
                    command = "powershell -Command \"Get-ChildItem -Path 'C:\\' -Recurse -Include 'chrome.exe' -ErrorAction SilentlyContinue | Select-Object -First 1 -ExpandProperty FullName\"";
                } else if ("msedge".equals(browser)) {
                    command = "powershell -Command \"Get-ChildItem -Path 'C:\\' -Recurse -Include 'msedge.exe' -ErrorAction SilentlyContinue | Select-Object -First 1 -ExpandProperty FullName\"";
                }else {
                    throw new RuntimeException("不支持的浏览器");
                }
                Process process = Runtime.getRuntime().exec(command);
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                String browserPath = null;
                while ((line = reader.readLine()) != null) {
                    browserPath=line;
                }
                String version = queryBrowserVersion(browserPath);
                builder.path(browserPath);
                builder.name(browser);
                builder.version(version);
                CacheUtils.browserName = browser;
                CacheUtils.version = version;
                CacheUtils.path = browserPath;
                return builder.build();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private static boolean isPathExist(String path) {
        File file = new File(path);
        return file.exists();
    }

    private String getOsArch(){
        String architecture = System.getProperty("os.arch");
        if (architecture.contains("64")) {
            return "64";
        } else {
            return "32";
        }
    }
    private Boolean checkDriverExit(String path,String target){
        String currentDir = System.getProperty("user.dir");
        File folder = new File(currentDir, path);
        if (folder.exists() && folder.isDirectory()) {
            File exeFile = new File(folder, target);

            return exeFile.exists() && exeFile.isFile();
        }
        return false;
    }
    private String queryBrowserVersion(String path) throws IOException {
        String search = "powershell.exe -Command \"(Get-Item '" + path + "').VersionInfo\"";
        ProcessBuilder start = new ProcessBuilder("cmd", "/c", search);
        BufferedReader r1 = new BufferedReader(new InputStreamReader(start.start().getInputStream(), "GBK"));
        String l1;
        int count = 0;
        while ((l1 = r1.readLine()) != null) {
            count++;
            if(count==4){
                return l1.split(" ")[0];
            }
        }
        return null;
    }
    private static String findClosestVersionKey(List<String> keys, String targetVersion) {
        double targetVersionNumber = convertVersionToNumber(targetVersion);
        String closestKey = null;
        double closestVersionDiff = Double.MAX_VALUE;
        for (String key : keys) {
            String version = extractVersionFromKey(key);
            double versionNumber = convertVersionToNumber(version);
            double versionDiff = Math.abs(versionNumber - targetVersionNumber);

            if (versionDiff < closestVersionDiff) {
                closestVersionDiff = versionDiff;
                closestKey = key;
            }
        }
        return closestKey;
    }
    private static String extractVersionFromKey(String key) {
        return key.split("/")[0];
    }

    public static double convertVersionToNumber(String version) {
        String[] parts = version.split("\\.");

        double versionNumber = 0;
        for (int i = 0; i < parts.length; i++) {
            versionNumber += Integer.parseInt(parts[i]) / Math.pow(10, i * 3);
        }

        return versionNumber;
    }
}
