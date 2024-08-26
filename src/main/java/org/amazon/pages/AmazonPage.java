package org.amazon.pages;

import org.amazon.base.BasePage;
import org.amazon.utils.ElementUtil;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

public class AmazonPage extends BasePage {
    WebDriver driver;
    ElementUtil elementUtil;

    By searchBox = By.id("twotabsearchtextbox");
    By searchSubmitBtn = By.cssSelector("#nav-search-submit-text");
    By loadingIcon = By.cssSelector(".loading-spinner-class");
    By resultTitlesList = By.xpath("//div[@data-component-type='s-search-result']//div[@data-cy='title-recipe']//span[contains(@class, 'a-text-normal')]");
    By resultPriceWholeList = By.xpath("//div[@data-component-type='s-search-result']//span[@class='a-price']//span[@class='a-price-whole']");
    By priceFractionList  = By.xpath("//div[@data-component-type='s-search-result']//span[@class='a-price']//span[@class='a-price-fraction']");
    By resultAddToCartList = By.xpath("//div[@data-component-type='s-search-result']//button[text()='Add to cart']");
    By nextPageButton = By.cssSelector(".s-pagination-next");
    By cartBtn = By.id("nav-cart-count-container");
    By cartCount = By.id("nav-cart-count");
    By totalItemInfo = By.id("sc-subtotal-label-buybox");
    By totalCost = By.cssSelector("#sc-subtotal-amount-buybox span");
    By quantityDropDown = By.id("quantity");
    By addToCartBtn = By.id("add-to-cart-button");
    By goToCart = By.id("#sw-gtc");
    By lastProductPrice = By.xpath("(//div[@id='corePrice_feature_div'] //*[@class='a-offscreen'])[1]");


    public AmazonPage(WebDriver driver) {
        this.driver = driver;
        elementUtil = new ElementUtil(driver);
    }

    public void verifyUrl(){
        String currentUrl = elementUtil.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("amazon.com"));
    }

    public void verifyPageLoadedFully(){
        String pageLoadStatus = elementUtil.pageReadyState();
        Assert.assertEquals(pageLoadStatus, "complete");
    }

    public void searchProduct(String productName){
        elementUtil.waitUntilElementToBeClickable(searchBox);
        elementUtil.doSendKeys(searchBox, productName);
        elementUtil.doClick(searchSubmitBtn);
    }

    public void verifyProductTitlesOnEveryResultPage(String productName){
        List<String> titlesNotContainingProduct = new ArrayList<>();
        WebElement lastProductTitle = null;
        int count = 1;
        while (true) {
            System.out.println("Amazon Page: "+ count);
            elementUtil.waitUntilInvisibilityOfElementLocated(loadingIcon);

            List<WebElement> itemTitleResults= elementUtil.waitUntilVisibilityOfAllElementsLocatedBy(resultTitlesList);
            lastProductTitle = itemTitleResults.get(itemTitleResults.size() - 1);
            elementUtil.scrollIntoView(lastProductTitle);
            System.out.println("Number of product: " + itemTitleResults.size());
            System.out.println("---------------------");
            for (WebElement item : itemTitleResults) {
                elementUtil.scrollIntoView(item);
                elementUtil.flash(item);
                String productTitle = item.getText();
                boolean isContain = productTitle.toLowerCase().contains(productName.toLowerCase());
                if (!isContain) {
                    titlesNotContainingProduct.add(productTitle);
                    continue;
                }
                Assert.assertTrue(isContain);
            }

            String val = elementUtil.getAttribute(nextPageButton, "class");
            if(val.contains("disabled")){
                break;
            }
            count++;
            elementUtil.doClick(nextPageButton);
        }

        if (!titlesNotContainingProduct.isEmpty()) {
            System.out.println("Titles not containing the product name:");
            for (String title : titlesNotContainingProduct) {
                System.out.println(title);
            }
        }
    }

    public String addLastProductToCart(){
        List<WebElement> itemTitleResults= elementUtil.waitUntilVisibilityOfAllElementsLocatedBy(resultTitlesList);
        List<WebElement> itemPriceWholeResults= elementUtil.formWebElements(resultPriceWholeList);
        WebElement lastProductPriceWhole = itemPriceWholeResults.get(itemPriceWholeResults.size() - 1);
        List<WebElement> itemPriceFractionResults= elementUtil.formWebElements(priceFractionList);
        WebElement lastProductPriceFraction = itemPriceFractionResults.get(itemPriceFractionResults.size() - 1);
        List<WebElement> itemAddCartButtonsResults= elementUtil.formWebElements(resultAddToCartList);
        WebElement lastAddToCartButton = itemAddCartButtonsResults.get(itemAddCartButtonsResults.size() - 1);
        System.out.println("Last product's price: " + lastProductPriceWhole.getText() + "." + lastProductPriceFraction.getText());
        lastAddToCartButton.click();
        return lastProductPriceWhole.getText() + "." + lastProductPriceFraction.getText();
    }

    public void navigateToShoppingCart() throws InterruptedException {
        Thread.sleep(2000);
        elementUtil.waitUntilTextToBe(cartCount, "1");
        elementUtil.waitUntilElementToBeClickable(cartBtn);
        elementUtil.scrollIntoView(elementUtil.formWebElement(cartBtn));
        elementUtil.doClick(cartBtn);

    }

    public void setProductQuantity(int quantity){
        elementUtil.waitUntilElementToBeClickable(quantityDropDown);
        elementUtil.selectFromDropDownText(quantityDropDown, String.valueOf(quantity));
        elementUtil.waitUntilElementToBeClickable(quantityDropDown);
    }

    public void verifyQuantityAndTotalPrice(int quantity, String productPrice){
        elementUtil.waitUntilTextToIncludeValue(totalItemInfo, String.valueOf(quantity));
        elementUtil.waitUntilVisibilityOfElementLocated(totalItemInfo);
        elementUtil.waitUntilVisibilityOfElementLocated(totalCost);
        Assert.assertEquals(elementUtil.doGetText(totalItemInfo).trim(), "Subtotal (" + quantity + " items):");
        Assert.assertEquals(elementUtil.doGetText(totalCost), "$"+ quantity * Double.parseDouble(productPrice) +"");
    }
}
