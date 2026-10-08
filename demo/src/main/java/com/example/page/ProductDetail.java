package com.example.page;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProductDetail extends BasePage {
    private By size41=By.xpath("//button[normalize-space()='OS']");
    By addToBagButton = By.xpath("//button[normalize-space()='Add to Bag']");
    By badgeLocator = By.cssSelector("span.absolute.-right-0\\.5.-top-0\\.5");
    private By cancelBtn=  By.xpath("//button[normalize-space()='Hủy bỏ']");

    private By iconCart = By.cssSelector("a[aria-label='Cart']");
    public ProductDetail(WebDriver driver)
    {
        super(driver);
    }
    public ProductDetail clickChooseSize()
    {
        waitForClickable(size41).click();
        return this;
    }
    public ProductDetail clickAddToCart()
    {
        waitForClickable(addToBagButton).click();
        return this;
    }
    public ProductDetail clickCancel()
    {
          waitForClickable(cancelBtn).click();
          return this;

    }

    public int getCartBadgeCount()
    {
        
        try
        {
            String text=waitForVisible(badgeLocator).getText().trim();
            return Integer.parseInt(text); // convert to integer
        }
        catch(Exception e)
        {
            return 0;
        }
    } 
    public ProductDetail verifyBadgeIncremented(int variable)
    {
        String expectedCount = String.valueOf(variable + 1);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(badgeLocator, expectedCount));
        return this;
        // this function we have to  capture  first value of icon bag then we use wait explicity to wait it +1 
    }


    
    public CartPage clickCart()
    {
        TypeClick(iconCart);
        return new CartPage(driver); /// this respresent for -> class not -> type data 
    }
}
