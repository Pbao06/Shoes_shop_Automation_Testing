package com.example.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.By;

public class HomePage extends BasePage {
    private By cardProduct = By.cssSelector("a[href='/product/4']");
    private By searchIcon= By.cssSelector("a[aria-label='Search']");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public ProductDetail clickProductDetail() {
        waitForClickable(cardProduct).click();
         wait.until(ExpectedConditions.urlContains("/product/4"));
        return new ProductDetail(driver);
    }
    public SearchPage clickSearch()
    {
        waitForClickable(searchIcon).click();
        wait.until(ExpectedConditions.urlContains("/search"));
        return new SearchPage(driver);
    }
}
