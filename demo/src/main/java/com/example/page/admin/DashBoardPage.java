package com.example.page.admin;
import com.example.page.*;

 import org.openqa.selenium.WebDriver;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DashBoardPage extends BasePage {
    private By productPage= By.xpath("//a[@href='/admin/products']");
    private By orderPage= By.cssSelector("a[href='/admin/order']");

    public DashBoardPage(WebDriver driver)
    {
        super(driver);
    }
    public Products clickProducts()
    {
        waitForVisible(productPage).click();
        wait.until(ExpectedConditions.urlContains("/products"));
        return new Products(driver);
    }
    public OrderPage clickOrder()
    {
        waitForVisible(orderPage).click();
        wait.until(ExpectedConditions.urlContains("/order"));
        return new OrderPage(driver);
    }
}
