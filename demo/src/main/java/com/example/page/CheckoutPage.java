package com.example.page;

import org.openqa.selenium.By;
import com.example.models.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CheckoutPage extends BasePage {
    private By firstName = By.cssSelector("input[name='firstName']");
    private By lastName = By.cssSelector("input[name='lastName']");
    private By email = By.cssSelector("input[name='email']");
    private By address = By.cssSelector("input[name='address']");
    private By city = By.cssSelector("input[name='city']");
    private By postalCode = By.cssSelector("input[name='postalCode']");
    private By selectCountry = By.cssSelector("select[name='country']");

    private By radioPayment = By.cssSelector(" input[type='radio']");

    private By optionUK = By.xpath("//option[normalize-space()='United Kingdom']");

    private By btnOrder = By.xpath("//button[normalize-space()='Place order']");

    // get static error this field require
    private By Errors = By.xpath("//p[normalize-space()='This field is required.']");

    // after they bought product need to -> go on Order page to check infomation of
    // product exacply what they order
    private By iconUser = By.cssSelector("button[aria-label='Account']");

    // pick information specific -> of product/id=2
    private By nameProduct = By.xpath("//aside//p[normalize-space()='Signature Leather Bag']");
    private By orderInfo = By.xpath("//aside//p[normalize-space()='Signature Leather Bag']/../p[2]");
    private By totalPriceProduct = By.xpath("//span[normalize-space()='Total']/../span[2]");

    private By myOrder=By.xpath("//a[@data-testid='my-orders-link' and @href='/orders' ]");

    public String getOrderInfo() {
        return waitForVisible(orderInfo).getText();
    }

    public String getSizeProduct() {
        String info = getOrderInfo();

        int start = info.indexOf("Size");
        int end = info.indexOf("· Qty");

        if (start == -1 || end == -1) {
            throw new IllegalStateException(
                    "Unexpected order info format: " + info);
        }

        start += "Size".length();

        if (start > end) {
            throw new IllegalStateException(
                    "Invalid size boundaries: " + info);
        }

        return info.substring(start, end).trim();
    }

    public int getQuantityProduct() {
        String info = getOrderInfo();
        String marker = "· Qty";

        int start = info.indexOf(marker);

        if (start == -1) {
            throw new IllegalStateException(
                    "Quantity marker not found: " + info);
        }

        start += marker.length();

        return Integer.parseInt(info.substring(start).trim());
    }

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public CheckoutPage TypeInfomation(String first, String last, String emails, String address_, String cityvalue,
            String postal, String optionCountry, String optionPayment, String ErrorMessage) {
        visiableToSendData(firstName, first);
        visiableToSendData(lastName, last);
        visiableToSendData(email, emails);
        visiableToSendData(address, address_);
        visiableToSendData(city, cityvalue);
        visiableToSendData(postalCode, postal);
        countryDropDown(optionCountry);
        clickRadioTypePayment(optionPayment);
        waitForClickable(btnOrder).click();

        waitForVisible(myOrder); // instead of we wait url -> here we wait a element of another page 
        return this;
    }

    public void countryDropDown(String value) {
        String xpathTemplate = "//option[normalize-space()='{value}']";
        String dynamicString = xpathTemplate.replace("{value}", value);
        By optionLocator = By.xpath(dynamicString);
        TypeClick(selectCountry);
        waitForVisible(optionLocator).click();
    }

    public CheckoutData getInfoProduct() {
        String name = waitForVisible(nameProduct).getText();
        String size = getSizeProduct();
        int quantity = getQuantityProduct();
        String totalPrice = waitForVisible(totalPriceProduct).getText().trim();
        return new CheckoutData(name, quantity, size, totalPrice);
    }

    public void clickRadioTypePayment(String value) {
        String xpathTemplate = "//label[contains(normalize-space(), '{value}')]/input[@type='radio']";
        String dynamicString = xpathTemplate.replace("{value}", value);
        By optionPayment = By.xpath(dynamicString);
        driver.findElement(optionPayment).click();
    }

    public String getEmptyError(String value) {
        String xpathError = "//p[normalize-space()='{value}']";
        String dynamicString = xpathError.replace("{value}", value);
        By address = By.xpath(dynamicString);
        return waitForVisible(address).getText(); // right here looking for this value parameter
    }

    public OrderCustomerPage clickOrderPage() {
        waitForVisible(myOrder).click();
        return new OrderCustomerPage(driver);
    }
}
