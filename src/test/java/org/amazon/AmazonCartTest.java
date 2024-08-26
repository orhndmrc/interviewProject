package org.amazon;

import org.amazon.base.BasePage;
import org.amazon.pages.AmazonPage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


public class AmazonCartTest {

    WebDriver driver;
    BasePage basePage;
    AmazonPage amazonPage;


    @BeforeMethod
    public void setUp()  {
        basePage = new BasePage();
        driver= basePage.getDriver();
        amazonPage = new AmazonPage(driver);
    }

    @AfterMethod
    public void tearDown()  {
        basePage.quitBrowser();
    }

    @Test
    @Parameters({"productName", "quantity"})
    public void verifyAddToCartWorkflow(String productName, int quantity) throws InterruptedException {
        amazonPage.verifyUrl();
        amazonPage.verifyPageLoadedFully();
        amazonPage.searchProduct(productName);
        amazonPage.verifyProductTitlesOnEveryResultPage(productName);
        String lastProductPrice = amazonPage.addLastProductToCart();
        amazonPage.navigateToShoppingCart();
        amazonPage.setProductQuantity(quantity);
        amazonPage.verifyQuantityAndTotalPrice(quantity, lastProductPrice);
    }

}



