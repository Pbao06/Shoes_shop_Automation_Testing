package com.example.testing;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.example.page.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.testng.Assert.*;
public class HomeTest extends BaseTest {

    
    @Test
    public void search_page()
    {
        driver.get("https://shoes-shop-bice.vercel.app/home");

         new HomePage(driver).clickSearch();

        
         assertTrue(driver.getCurrentUrl().contains("Search"));
    }
    // @Test
    // public void Test_ProductDetail()
    // {
    //     driver.get("https://shoes-shop-bice.vercel.app/home");

    //     new HomePage(driver).clickProductDetail();
    //     wait.until(ExpectedConditions.urlContains("/product/4"));
    //      assertTrue(driver.getCurrentUrl().contains("/product/4"));
    // }
}
