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

public class ProductDetailTest extends BaseTest {
    @Test
    public void Flow_AddToCart_() 
    {
        openUrl("/login"); // get driver at baselink + login home 
        ProductDetail productDetail = new LoginPage(driver).typeLogin("admin@example.com", "Admin@123").clickProductDetail().clickChooseSize().clickAddToCart();
        //  wait.until(ExpectedConditions.urlContains("/home"));
        // 2. Capture initial badge count (e.g., 2)
        int initialCount = productDetail.getCartBadgeCount(); // getting current number on icon bag 
        productDetail.clickAddToCart()
                 .verifyBadgeIncremented(initialCount);
                 int updatedCount = productDetail.getCartBadgeCount(); // this is new number when updated 
        assertEquals(updatedCount,initialCount+1," Not success ");
    }
}
