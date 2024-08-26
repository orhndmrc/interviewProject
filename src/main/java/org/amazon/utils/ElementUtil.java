package org.amazon.utils;

import org.amazon.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.*;
import org.openqa.selenium.*;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class ElementUtil extends BasePage {
    WebDriver driver;

    //Constructor
    public ElementUtil (WebDriver driver){
        this.driver = driver;
    }

    /**
     * Forms web elements
     * @param locator
     * @return
     */
    public WebElement formWebElement(final By locator){
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(15))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(ElementClickInterceptedException.class)
                .withMessage(
                        "Webdriver waited for 15 seconds but still could not find the element therefore TimeOutException has been thrown"
                );
        return wait.until(new Function<WebDriver, WebElement>(){
            public WebElement apply(WebDriver driver){
                return driver.findElement(locator);
            }
        });
    }

    public WebElement waitUntilTextToIncludeValue(By locator, String textToInclude){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement element = wait.until(new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver d) {
                WebElement elem = formWebElement(locator);
                if (elem.getText().contains(textToInclude)) {
                    return elem;
                } else {
                    return null;
                }
            }
        });
        return element;
    }

    public List<WebElement> formWebElements(By locator){
        return driver.findElements(locator);
    }


    public void doClick(By locator){
        try{
            formWebElement(locator).click();
        }
        catch(Exception e){
            System.out.println("Some exception occured while click on  webelement " +locator);
        }
    }


    public void doSendKeys(By locator, String value){
        try{
            formWebElement(locator).clear();
            formWebElement(locator).sendKeys(value);
        }
        catch(Exception e){
            System.out.println("Some exception occured while sending to  webelement " + locator);
        }
    }

    public String doGetText(By locator){
        String text = null;
        try {
            text = formWebElement(locator).getText();
        } catch (Exception e) {
            System.out.println("Some exception occured while get text to webelement "+ locator);
        }
        return text;
    }

    public String getCurrentUrl(){
        String text = null;
        try {
            text = driver.getCurrentUrl();
        } catch (Exception e) {
            System.out.println("Some exception occured while geting current url "+ e);
        }
        return text;
    }

    public String getAttribute(By locator, String attributeValue){
        String text = null;
        try {
            text = formWebElement(locator).getAttribute(attributeValue);
        } catch (Exception e) {
            System.out.println("Some exception occured while geting attribute value "+ e);
        }
        return text;
    }

    public void scrollIntoView(WebElement element) {
        JavascriptExecutor js = ((JavascriptExecutor) driver);
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public String pageReadyState() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return (String) js.executeScript("return document.readyState");
    }

    public void waitUntilElementToBeClickable(By locator){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void waitUntilElementToBeClickableWebElement(WebElement element){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public void waitUntilTextToBe(By locator, String text ){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.textToBe(locator, text));
    }

    public void waitUntilInvisibilityOfElementLocated(By locator){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public List<WebElement> waitUntilVisibilityOfAllElementsLocatedBy(By locator){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public void waitUntilVisibilityOfElementLocated(By locator){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }


    public void flash(WebElement element) {
        JavascriptExecutor js = ((JavascriptExecutor) driver);
        String bgcolor = element.getCssValue("backgroundColor");
        for (int i = 0; i < 20; i++) {
            changeColor("rgb(0,200,0)", element);
            changeColor(bgcolor, element);
        }
    }

    public void changeColor(String color, WebElement element) {
        JavascriptExecutor js = ((JavascriptExecutor) driver);
        js.executeScript("arguments[0].style.backgroundColor = '" + color + "'", element);

        try {
            Thread.sleep(1);
        }
        catch (InterruptedException e) {
            System.out.println("Something went wrong..." + e);
        }
    }

    public void selectFromDropDownText(By locator, String dropDownText ){
        WebElement text = formWebElement(locator);
        Select selectText = new Select(text);
        selectText.selectByVisibleText(dropDownText);
    }
}
