package com.example.page;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.By;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends BasePage {
    private By inputEmail = By.cssSelector("input[type='email']");
    private By inputPassword = By.cssSelector("input[type='password']");
    private By clickButton = By.cssSelector("button[type='submit']");
    // locator error response
    private By errorResponse = By.cssSelector("p[role='alert']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public HomePage typeLogin(String email, String pass) {
        TypeInput(inputEmail, email);
        TypeInput(inputPassword, pass);
        driver.findElement(clickButton).click();
        wait.until(ExpectedConditions.urlContains("/home"));
        return new HomePage(driver);
    }

    public String getTextError() {
        return waitForVisible(errorResponse).getText();
    }
}
