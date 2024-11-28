package io.github.geniusay.strategy.dirver;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static io.github.geniusay.strategy.dirver.DriverFactory.DriverType.Chrome;

public class DriverFactory {
    public static  enum DriverType{
        Edge,
        Chrome
    }

    public static ChromiumDriver getDriver(String driverPath, String browserPath, DriverType driverType,Boolean debug){
        switch (driverType){
            case Edge: return edgeDriver(driverPath, browserPath,debug);
            case Chrome: return chromeDriver(driverPath, browserPath,debug);
            default: return edgeDriver(driverPath, browserPath,debug);
        }
    }

    public static ChromeDriver chromeDriver(String driverPath, String browserPath,Boolean debug){
        System.setProperty("webdriver.chrome.driver", driverPath);
        ChromeOptions options = new ChromeOptions();
        options.setBinary(browserPath);
        if(debug){
            options.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");
        }
        return  new ChromeDriver(options);
    }

    public static EdgeDriver edgeDriver(String driverPath, String browserPath,Boolean debug){
        System.setProperty("webdriver.edge.driver", driverPath);
        EdgeOptions options = new EdgeOptions();
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36");
        options.addArguments("--remote-allow-origins=*");
        if(debug){
            options.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");
        }
        return new EdgeDriver(options);
    }

    public static DriverType driverType(String browserPath){
        browserPath = browserPath.toLowerCase();
        if(browserPath.endsWith("msedge.exe")){
            return DriverType.Edge;
        } else if (browserPath.endsWith("chrome.exe")) {
            return Chrome;
        }
        return DriverType.Edge;
    }

}
