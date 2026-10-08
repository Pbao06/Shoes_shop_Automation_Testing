package com.example.page;
import com.example.page.admin.*;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.By;

public class HomePage extends BasePage {
    private By cardProduct = By.cssSelector("a[href='/product/4']");
    private By searchIcon= By.cssSelector("a[aria-label='Search']");

    private By accountIcon=By.cssSelector("button[aria-label='Account']");
    private By accountDropDown=By.xpath("//a[@href='/admin' and normalize-space()='Admin']");

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
    public DashBoardPage clickAdmin()
    {
         TypeClick(accountIcon);
         waitForClickable(accountDropDown).click();
         wait.until(ExpectedConditions.urlContains("/admin")); 
        return new DashBoardPage(driver);
    }
}
