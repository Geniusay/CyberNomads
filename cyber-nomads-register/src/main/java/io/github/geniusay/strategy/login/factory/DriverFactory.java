package io.github.geniusay.strategy.login.factory;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.openqa.selenium.chromium.ChromiumOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/17 3:35
 */
public class DriverFactory {

    public static ChromiumDriver getDriver(String name){
        if("edge".equals(name)){
            return new EdgeDriver();
        }else if("chrome".equals(name)){
            return new ChromeDriver();
        }
        return null;
    }

    public static ChromiumOptions getOptions(String name){
        if("edge".equals(name)){
            return new EdgeOptions();
        }else if("chrome".equals(name)){
            return new ChromeOptions();
        }
        return null;
    }

}
