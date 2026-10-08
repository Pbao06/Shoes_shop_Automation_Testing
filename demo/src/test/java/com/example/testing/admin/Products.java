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
public class Products extends BaseTest {
    @Test
    public void test()
    {
        openUrl("/login");
         var test=new LoginPage(driver).typeLogin("admin@example.com","Admin@123").clickAdmin().clickProducts().SearchData().getNameProduct();
         String expected="Classic Leather Loafer";
         assertEquals(test.toUpperCase(),expected.toUpperCase()," DO not match lol dumb");
    }
}
