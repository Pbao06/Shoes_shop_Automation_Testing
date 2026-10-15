package com.example.page;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class CartPage extends BasePage {

    private By btnCheckOut = By.xpath("//button[normalize-space()='Checkout']");

    public CartPage(WebDriver driver)
    {
        super(driver);
    }
    public CheckoutPage clickCheckout()
    { 
        waitForClickable(btnCheckOut).click();
        wait.until(ExpectedConditions.urlContains("/checkout"));
        return  new CheckoutPage(driver);  
    }

}
