package com.example.testing;

import com.example.page.LoginPage;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.testng.Assert.*;

public class LoginTest extends BaseTest {
    @Test
    public void SuccessLogin() {
        driver.get(urlbase + "/login");
        new LoginPage(driver).typeLogin("admin@example.com", "Admin@123");
        wait.until(ExpectedConditions.urlContains("/home")); 
        assertTrue(driver.getCurrentUrl().contains("/home"));
        // spend to test case it around exacly we need 20 secons
    }

    // @Test
    // public void EmptyEmail() {
    //     driver.get(urlbase + "/login");
    //     String title = new LoginPage(driver).typeLogin("", "Admin@123").getTextError();
    //     assertEquals(title, "Request failed with status 400"," Incorrect");
    // }
    // @Test 
    // public void WrongPassOrEmail()
    // {
    //     driver.get(urlbase+"/login");
    //     String title= new LoginPage(driver).typeLogin("fdfdfhj@example.com","Admin@123").getTextError();
    //     assertEquals(title,"Email hoặc mật khẩu không đúng.", "incorrect ");
    // }
    
}
