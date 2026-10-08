package com.example.page;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CheckoutPage  extends BasePage{
     private By firstName=By.cssSelector("input[name='firstName']");
    private By lastName=By.cssSelector("input[name='lastName']");
    private By email=By.cssSelector("input[name='email']");
     private By address=By.cssSelector("input[name='address']");
     private By city=By.cssSelector("input[name='city']");
     private By postalCode=By.cssSelector("input[name='postalCode']");
     private By selectCountry=By.cssSelector("select[name='country']");

     private By radioPayment= By.cssSelector(" input[type='radio']");


     private By optionUK=By.xpath("//option[normalize-space()='United Kingdom']");


     private By btnOrder= By.xpath("//button[normalize-space()='Place order']");


     // get static error this field require 
     private By Errors=By.xpath("//p[normalize-space()='This field is required.']");
    public CheckoutPage(WebDriver driver )
    { 
        super(driver);
    }

    public CheckoutPage TypeInfomation(String first, String last , String emails , String address_ , String cityvalue,String postal,String optionCountry, String optionPayment,String ErrorMessage)
    {
         visiableToSendData(firstName, first);
         visiableToSendData(lastName, last);
         visiableToSendData(email, emails);
         visiableToSendData(address, address_);
         visiableToSendData(city, cityvalue);
         visiableToSendData(postalCode, postal);
         countryDropDown(optionCountry);
         clickRadioTypePayment(optionPayment);
         waitForClickable(btnOrder).click();
         return this;
    }
    public void countryDropDown(String value)
    {
        String xpathTemplate = "//option[normalize-space()='{value}']";
        String dynamicString= xpathTemplate.replace("{value}",value);
        By optionLocator = By.xpath(dynamicString);
        TypeClick(selectCountry);
        waitForVisible(optionLocator).click();
    } 

    public void clickRadioTypePayment(String value)
    {
        String xpathTemplate="//label[contains(normalize-space(), '{value}')]/input[@type='radio']";
        String dynamicString=xpathTemplate.replace("{value}",value);
        By optionPayment= By.xpath(dynamicString);
        driver.findElement(optionPayment).click();
    }
    public String getEmptyError( String value )
    {
        String xpathError= "//p[normalize-space()='{value}']";
        String dynamicString= xpathError.replace("{value}",value);
        By address= By.xpath(dynamicString);
        return waitForVisible(address).getText(); // right here looking for this value parameter 
    }
}
