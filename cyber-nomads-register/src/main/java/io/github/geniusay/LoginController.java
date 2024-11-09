package io.github.geniusay;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.catalina.User;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/27 14:46
 */
public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private String webDriver;
    private String browser;
    private static final String URL = "https://www.bilibili.com/";
    private HashMap<String, HashSet<Cookie>> COOKIES = new HashMap<>();

    public LoginController() {
    }
    @FXML
    private void handleLogin() throws InterruptedException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if(StringUtils.isBlank(username)|| StringUtils.isBlank(password)){
            showAlert("提示", "账号或密码不能为空");
        }
        System.setProperty("webdriver.chrome.driver", webDriver);
        ChromeOptions options = new ChromeOptions();
        options.setBinary(browser);
        ChromeDriver loginwebDriver = new ChromeDriver(options);
        loginwebDriver.get(URL); //
        loginwebDriver.manage().deleteAllCookies();
        WebDriverWait wait = new WebDriverWait(loginwebDriver, Duration.ofSeconds(120));
        WebElement login = loginwebDriver.findElement(By.xpath("/html/body/div[2]/div[2]/div[1]/div[1]/ul[2]/li[1]/li/div[1]/div"));
        login.click();
        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[5]/div/div[4]/div[2]/form/div[1]/input")));
        usernameInput.sendKeys(username);
        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[5]/div/div[4]/div[2]/form/div[3]/input")));
        passwordInput.sendKeys(password);
        WebElement loginButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[5]/div/div[4]/div[2]/div[2]/div[2]")));
        loginButton.click();
        WebElement userImg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div[2]/div[1]/div[1]/ul[2]/li[1]/div[1]/a[1]/picture/img")));
        if(userImg!=null){
            Set<Cookie> cookies = loginwebDriver.manage().getCookies();
            loginwebDriver.quit();
            ChromeDriver confirmLogin = new ChromeDriver(options);
            WebDriverWait confirmWait = new WebDriverWait(confirmLogin, Duration.ofSeconds(120));
            confirmLogin.get(URL);
            HashSet<Cookie> set = new HashSet<>();
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
            WebElement img = confirmWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div[2]/div[1]/div[1]/ul[2]/li[1]/div[1]/a[2]")));
            COOKIES.put(username, set);
            confirmLogin.quit();
        }else{
            throw new RuntimeException("登陆失败!");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void saveCookies(){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("账号Cookies"))) {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(COOKIES);
            System.out.println(json);
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void set(String webdriver,String browser){
        this.webDriver = webdriver.replace("\\","/");
        this.browser = browser.replace("\\","/");
    }

}
