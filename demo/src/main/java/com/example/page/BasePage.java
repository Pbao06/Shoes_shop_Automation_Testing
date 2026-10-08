package com.example.page;

import org.openqa.selenium.WebDriver;

import java.io.File;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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
    protected void visiableClearThenSendData(By locator,String value)
    {
         WebElement element = waitForVisible(locator);
    element.click();
    
    // Bôi đen tất cả và xóa (Dùng COMMAND cho macOS, CONTROL cho Windows/Linux)
    String osName = System.getProperty("os.name").toLowerCase();
    Keys controlOrCommand = osName.contains("mac") ? Keys.COMMAND : Keys.CONTROL;
    
    element.sendKeys(Keys.chord(controlOrCommand, "a"), Keys.BACK_SPACE);
    element.sendKeys(value);
    }
    protected void uploadFile(By locator, String filePath) {
        // Step 1: Wait for element to exist in DOM (even if hidden via CSS)
        WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        
        // Step 2: Convert relative path to absolute path if needed
        File file = new File(filePath);
        String absolutePath = file.getAbsolutePath();
        
        // Step 3: Send path directly to input element
        fileInput.sendKeys(absolutePath);
    }
    public void uploadFileAndWait(By inputLocator, String filePath, By resultLocator) {
        uploadFile(inputLocator, filePath);
        wait.until(ExpectedConditions.visibilityOfElementLocated(resultLocator));
    }
    // protected void visiableToSendData_Double(By locator,double value)
    // {
    //     waitForVisible(locator).sendKeys(value);
    // }
    // public String getTextError(By locator) {
    //      return waitForVisible(locator).getText();
    // }
}
