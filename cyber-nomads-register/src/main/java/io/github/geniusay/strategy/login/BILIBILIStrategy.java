package io.github.geniusay.strategy.login;

import io.github.geniusay.pojo.Platform;
import io.github.geniusay.service.UserService;
import io.github.geniusay.strategy.dirver.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/11 20:41
 */
@Component("bilibili")
public class BILIBILIStrategy extends AbstractLoginStrategy{

    private static final String URL = "https://www.bilibili.com/";

    @Resource
    UserService userService;
    @Override
    public String platform() {
        return Platform.BILIBILI.getPlatform();
    }

    @Override
    public String execute(String username) throws InterruptedException {
        String driverPath = changePath(userService.queryPathExist().getPathDTO().getDriverPath());
        String browserPath = changePath(userService.queryPathExist().getPathDTO().getBrowserPath());
        DriverFactory.DriverType driverType = DriverFactory.driverType(browserPath);
        ChromiumDriver loginWebDriver = DriverFactory.getDriver(driverPath, browserPath, driverType,false);
        loginWebDriver.get(URL);
        loginWebDriver.manage().deleteAllCookies();
        loginWebDriver.navigate().refresh();
        WebDriverWait wait = new WebDriverWait(loginWebDriver, Duration.ofSeconds(300));
        try {
            WebElement login = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div[2]/div[1]/div[1]/ul[2]/li[1]/li/div/div/span")));
            login.click();
            WebElement userImg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div[2]/div[1]/div[1]/ul[2]/li[1]/div[1]/a[1]/picture/img")));
            if(userImg!=null){
                Set<Cookie> cookies = loginWebDriver.manage().getCookies();
                loginWebDriver.manage().deleteAllCookies();
                Set<Cookie> set = new HashSet<>();
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
                loginWebDriver.navigate().refresh();
                WebElement img = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div[2]/div[1]/div[1]/ul[2]/li[1]/div[1]/a[1]/picture/img")));
                if(img!=null){
                    loginWebDriver.quit();
                    return set.stream().map(cookie -> cookie.getName() + "=" + cookie.getValue()).collect(Collectors.joining(";"));
                }
                return null;
            }else{
                return null;
            }
        }catch (Exception e){
            return null;
        }
    }
}
