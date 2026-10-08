package com.example.page;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RegisterPage extends BasePage {
    private By firstName=By.cssSelector("input[data-testid='register-first-name']");
    private By lastName=By.cssSelector("input[data-testid='register-last-name']");
    private By email=By.cssSelector("input[data-testid='register-email']");
    private By password=By.cssSelector("input[data-testid='register-password']");
    private By confirmPassword=By.cssSelector("input[data-testid='register-confirm-password']");
    private By buttonSubmit=By.cssSelector("button[data-testid='register-submit-button']");
    // get log error specific 
    public By validation=By.cssSelector("p[data-testid='register-validation-error']");




    public RegisterPage(WebDriver driver)
    {
        super(driver);
    }


    public RegisterPage Register(String first ,String last ,String mail , String pass ,String confirm)
    {
        TypeInput(firstName,first);
        TypeInput(lastName,last);
        TypeInput(email,mail);
        TypeInput(password,pass);
        TypeInput(confirmPassword,confirm);
        TypeClick(buttonSubmit);
        return this;
    }
    public String getTextError() {
         return waitForVisible(validation).getText();
    }
    
}