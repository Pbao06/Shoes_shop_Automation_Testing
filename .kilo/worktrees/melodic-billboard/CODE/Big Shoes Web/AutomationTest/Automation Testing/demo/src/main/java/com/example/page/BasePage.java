package com.example.page;

import org.openqa.selenium.WebDriver;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {

    protected WebDriver driver;

    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(50));
    }

    protected WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    protected void TypeClick(By locator)
    {
        driver.findElement(locator).click();
    }
    protected void TypeInput(By locator , String value)
    {
          driver.findElement(locator).sendKeys(value);
    }
    protected void visiableToSendData(By locator, String value)
    {
        waitForVisible(locator).sendKeys(value);
    }
    // public String getTextError(By locator) {
    //      return waitForVisible(locator).getText();
    // }
}
