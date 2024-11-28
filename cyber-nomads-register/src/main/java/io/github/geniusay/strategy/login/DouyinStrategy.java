package io.github.geniusay.strategy.login;

import io.github.geniusay.pojo.Platform;
import io.github.geniusay.service.UserService;
import io.github.geniusay.strategy.dirver.DriverFactory;
import io.github.geniusay.util.CacheUtils;
import okhttp3.Cache;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static io.github.geniusay.strategy.dirver.DriverFactory.DriverType.Edge;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/24 17:02
 */
@Component("douyin")
public class DouyinStrategy extends AbstractLoginStrategy{
    private static final String URL ="https://www.douyin.com/";
    @Resource
    UserService userService;
    @Override
    public String execute(String username) {
        String driverPath = changePath(userService.queryPathExist().getPathDTO().getDriverPath());
        String browserPath = changePath(userService.queryPathExist().getPathDTO().getBrowserPath());
        String command = "cmd /c start \"\" \"" + browserPath + "\" --remote-debugging-port=9222 --user-data-dir=\"C:\\selenium\\" + CacheUtils.browserName + "Profile\" " + URL;
        try {
            Process process = Runtime.getRuntime().exec(command);
            Thread.sleep(3000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            Set<Cookie> cookies;
            Set<Cookie> set = new HashSet<>();
            long timeout = 300;
            while (timeout>0){
                DriverFactory.DriverType driverType = DriverFactory.driverType(browserPath);
                ChromiumDriver loginWebDriver = DriverFactory.getDriver(driverPath, browserPath, driverType,true);
                System.out.println(loginWebDriver.getTitle());
                loginWebDriver.manage().deleteAllCookies();
                if(loginWebDriver.getPageSource().contains("退出登录")){
                    cookies = loginWebDriver.manage().getCookies();
                    for (Cookie cookie : cookies) {
                        if("".equals(cookie.getName())||cookie.getName()==null
                                ||cookie.getValue()==null|| "".equals(cookie.getValue())
                                ||cookie.getPath()==null||"".equals(cookie.getPath())) {
                            continue;
                        }
                        Cookie c = new Cookie(cookie.getName(),cookie.getValue());
                        set.add(c);
                        loginWebDriver.manage().addCookie(cookie);
                    }
                    loginWebDriver.manage().deleteAllCookies();
                    loginWebDriver.quit();
                    return set.stream().map(cookie -> cookie.getName() + "=" + cookie.getValue()).collect(Collectors.joining(";"));
                }
                timeout--;
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            return null;
        }
        return "";
    }

    @Override
    public String platform() {
        return Platform.DOUYIN.getPlatform();
    }
}
