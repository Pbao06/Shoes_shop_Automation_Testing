package com.example.testing.admin;
import com.example.testing.*;
import com.example.page.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.annotations.AfterMethod;
import static org.testng.Assert.*;
public class OrderTest extends BaseTest{
    @Test
    public void check_Url()
    {
        openUrl("/login");
         var test=new LoginPage(driver).typeLogin("admin@example.com","Admin@123").clickAdmin().clickOrder();
         assertTrue(driver.getCurrentUrl().contains("/order"));
    }
}
