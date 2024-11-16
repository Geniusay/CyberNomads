package io.github.geniusay.strategy.login;

import io.github.geniusay.pojo.Platform;
import io.github.geniusay.service.UserService;
import io.github.geniusay.util.HTTPUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Qualifier;
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
@Component
public class BILIBILIStrategy extends AbstractLoginStrategy{

    private static final String URL = "https://www.bilibili.com/";

    @Resource
    UserService userService;


    @Override
    public String platform() {
        return Platform.BILIBILI.getPlatform();
    }

    private String changePath(String str){
        return str.replace("\\","/");
    }

    @Override
    public String execute(String username) {
        String target1 = changePath(userService.queryPathExist().getPathDTO().getDriverPath());
        String target2 = changePath(userService.queryPathExist().getPathDTO().getBrowserPath());
        System.setProperty("webdriver.chrome.driver", target1);
        ChromeOptions options = new ChromeOptions();
        options.setBinary(target2);
        ChromeDriver loginWebDriver = new ChromeDriver(options);
        loginWebDriver.get(URL);
        loginWebDriver.manage().deleteAllCookies();
        WebDriverWait wait = new WebDriverWait(loginWebDriver, Duration.ofSeconds(120));
        try {
            WebElement login = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div[2]/div[1]/div[1]/ul[2]/li[1]/li/div/div/span")));
            login.click();
            WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[5]/div/div[4]/div[2]/form/div[1]/input")));
            usernameInput.sendKeys(username);
            Thread.sleep(500);
            WebElement passwordInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[5]/div/div[4]/div[2]/form/div[3]/input")));
            passwordInput.click();
            WebElement userImg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div[2]/div[1]/div[1]/ul[2]/li[1]/div[1]/a[1]/picture/img")));
            if(userImg!=null){
                Set<Cookie> cookies = loginWebDriver.manage().getCookies();
                loginWebDriver.quit();
                ChromeDriver confirmLogin = new ChromeDriver(options);
                WebDriverWait confirmWait = new WebDriverWait(confirmLogin, Duration.ofSeconds(60));
                confirmLogin.get(URL);
                Set<Cookie> set = new HashSet<>();
                confirmLogin.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                confirmLogin.manage().deleteAllCookies();
                for (Cookie cookie : cookies) {
                    if("".equals(cookie.getName())||cookie.getName()==null
                            ||cookie.getValue()==null|| "".equals(cookie.getValue())
                            ||cookie.getPath()==null||"".equals(cookie.getPath())) {
                        continue;
                    }
                    Cookie c = new Cookie(cookie.getName(),cookie.getValue());
                    set.add(c);
                    confirmLogin.manage().addCookie(cookie);
                }
                confirmLogin.navigate().refresh();
                WebElement img = confirmWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div[2]/div[1]/div[1]/ul[2]/li[1]/div[1]/a[1]/picture/img")));
                if(img!=null){
                    confirmLogin.quit();
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
