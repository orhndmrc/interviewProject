package org.amazon.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {
    public WebDriver driver;

    public WebDriver getDriver(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
        driver.get("https://www.amazon.com/");
        return driver;
    }

    public void quitBrowser(){
        try{
            driver.quit();
        }
        catch(Exception e){
            System.out.println("Some exception occured while quitting the browser");
        }
    }
}
