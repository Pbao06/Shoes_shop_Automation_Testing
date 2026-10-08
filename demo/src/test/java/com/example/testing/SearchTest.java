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
public class SearchTest extends BaseTest {
    @Test
    public void search_specific()
    {
        openUrl("home");
        var search= new HomePage(driver).clickSearch();
        search.sendDataToLink("shoes"); // show infomation of all products releated to shoes
        search.clickProductFound();
        wait.until(ExpectedConditions.urlContains("/product/3"));
        assertTrue(driver.getCurrentUrl().contains("/product/3"));
    }
}
