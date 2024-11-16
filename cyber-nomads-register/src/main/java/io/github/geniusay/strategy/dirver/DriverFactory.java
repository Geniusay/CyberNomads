package io.github.geniusay.strategy.dirver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class DriverFactory {
    public static enum DriverType{
        Edge,
        Chrome
    }

    public static ChromiumDriver getDriver(String driverPath, String browserPath, DriverType driverType){
        switch (driverType){
            case Edge: return edgeDriver(driverPath, browserPath);
            case Chrome: return chromeDriver(driverPath, browserPath);
            default: return edgeDriver(driverPath, browserPath);
        }
    }

    public static ChromeDriver chromeDriver(String driverPath, String browserPath){
        System.setProperty("webdriver.chrome.driver", driverPath);
        ChromeOptions options = new ChromeOptions();
        options.setBinary(browserPath);
        return  new ChromeDriver(options);
    }

    public static EdgeDriver edgeDriver(String driverPath, String browserPath){
        System.setProperty("webdriver.edge.driver", driverPath);
        EdgeOptions options = new EdgeOptions();
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36");
        options.addArguments("--remote-allow-origins=*");
        return new EdgeDriver(options);
    }

    public static DriverType driverType(String browserPath){
        if(browserPath.endsWith("msedge.exe")){
            return DriverType.Edge;
        } else if (browserPath.endsWith("chrome.exe")) {
            return DriverType.Chrome;
        }
        return DriverType.Edge;
    }
}
