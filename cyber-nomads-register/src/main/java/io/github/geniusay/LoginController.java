package io.github.geniusay;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
    private static final String URL = "https://www.bilibili.com/";
    private List<HashSet<Cookie>> saveCookies = new ArrayList<>();
    private HashMap<String,HashSet<Cookie>> userCookies = new HashMap<>();
    @FXML
    private void handleBack() {
        // 回退逻辑，示例中只是显示提示
        showAlert("Back", "Returning to the previous screen...");
    }
    @FXML
    private void handleLogin() throws InterruptedException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        System.setProperty("webdriver.chrome.driver", "D:\\downLoad\\chromedriver_win32\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.setBinary("C:\\Program Files (x86)\\Chromebrowser\\Chrome.exe");
        ChromeDriver loginwebDriver = new ChromeDriver(options);
        loginwebDriver.get(URL); //
        loginwebDriver.manage().deleteAllCookies();
        WebElement login = loginwebDriver.findElement(By.xpath("/html/body/div[2]/div[2]/div[1]/div[1]/ul[2]/li[1]/li/div/div/span"));
        login.click();
        Thread.sleep(1000);
        WebElement usernameInput = loginwebDriver.findElement(By.xpath("/html/body/div[4]/div/div[4]/div[2]/form/div[1]/input"));
        usernameInput.sendKeys(username);
        WebElement passwordInput = loginwebDriver.findElement(By.xpath("/html/body/div[4]/div/div[4]/div[2]/form/div[3]/input"));
        passwordInput.sendKeys(password);
        WebElement loginButton = loginwebDriver.findElement(By.xpath("/html/body/div[4]/div/div[4]/div[2]/div[2]/div[2]"));
        loginButton.click();
        Thread.sleep(15000L);
        Set<Cookie> cookies = loginwebDriver.manage().getCookies();
        loginwebDriver.quit();
        System.out.println(cookies);
        ChromeDriver confirmLogin = new ChromeDriver(options);
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
            set.add(cookie);
            confirmLogin.manage().addCookie(cookie);
        }
        confirmLogin.navigate().refresh();
        Thread.sleep(2000L);
        WebElement avator = confirmLogin.findElement(By.xpath("/html/body/div[2]/div[2]/div[1]/div[1]/ul[2]/li[1]"));
        if(avator!=null){
            System.out.println("登陆成功!");
            userCookies.put(username,set);
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
        System.out.println(saveCookies);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("账号Cookies"))) {
            StringBuilder sb = new StringBuilder();
            for (HashMap.Entry<String, HashSet<Cookie>> entry : userCookies.entrySet()) {
                String key = entry.getKey();
                HashSet<Cookie> cookies = entry.getValue();
                sb.append(key).append(":").append(cookies.toString()).append(", ");
            }

            // 移除最后的逗号和空格
            if (sb.length() > 0) {
                sb.setLength(sb.length() - 2);
            }

            // 写入文件
            writer.write("{" + sb + "}");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void quit(){
        Platform.exit();

        // 退出 JVM
        System.exit(0);
    }
}
