package com.example.testing;

import org.openqa.selenium.support.ui.WebDriverWait;
import com.example.page.*;
import com.example.models.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.testng.Assert.*;

public class CheckoutTest extends BaseTest {

    @DataProvider(name = "CheckoutErrorData")
    public Object[][] getRegisterErrorData() {
        return new Object[][] {
                { new Checkout("", "Phan", "Bao@example.com", " Phan Dinh Phung Q1 district", "Ho Chi Minh", "232",
                        "United Kingdom", "PayPal", "This field is required.") },
                 { new Checkout("f", "Phan", "Bao@example.com", " Phan Dinh Phung Q1 district", "Ho Chi Minh", "232",
                 "United Kingdom", "PayPal", "First name must be at least 2 characters.") },
                 { new Checkout("Nguyễn Thị Hoài Thanh Hoàng Trúc Trân Princess Nguyễn Thị Ngọc Anh", "Phan", "Bao@example.com", " Phan Dinh Phung Q1 district", "Ho Chi Minh", "232",
                 "United Kingdom", "PayPal", "First name must be at most 50 characters.") },
                 { new Checkout("Nguyễn Thị Hoài ", "Phan", "@example.com", " Phan Dinh Phung Q1 district", "Ho Chi Minh", "232",
                 "United Kingdom", "PayPal", "Please enter a valid email address.") }, // lack of name email 
                 { new Checkout("Nguyễn Thị Hoài ", "Phan", "Nameexample.com", " Phan Dinh Phung Q1 district", "Ho Chi Minh", "232",
                 "United Kingdom", "PayPal", "Please enter a valid email address.") }, // lack of @ 
                 { new Checkout("Nguyễn Thị Hoài ", "Phan", "Name@exampl", " Phan Dinh Phung Q1 district", "Ho Chi Minh", "232",
                 "United Kingdom", "PayPal", "Please enter a valid email address.") }, // lack of domain email
                 { new Checkout("Nguyễn Thị Hoài ", "Phan", "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijkl@domain.com",
                  " Phan Dinh Phung Q1 district", "Ho Chi Minh", "232",
                 "United Kingdom", "PayPal", "Email address must be at most 254 characters.") }, // email > 254 
        };
    }

    @Test(dataProvider = "CheckoutErrorData") // <-- This connects the DataProvider to the test
    public void Checkout_Success_test(Checkout data) {

        openUrl("/login"); // Navigate to base URL + /login

        ProductDetail productDetail = new LoginPage(driver)
                .typeLogin("admin@example.com", "Admin@123")
                .clickProductDetail()
                .clickChooseSize()
                .clickAddToCart();

        // 1. Capture initial badge count
        int initialCount = productDetail.getCartBadgeCount();

        // 2. Add to cart again and verify badge increments
        productDetail.clickAddToCart()
                .verifyBadgeIncremented(initialCount);

        // 3. Navigate to Cart -> Checkout -> Fill order info & Submit
        String actualError = productDetail.clickCart()
                .clickCheckout()
                .TypeInfomation(data.getFirstName(), data.getLastName(), data.getEmail(), data.getAddress(),
                        data.getCity(), data.getPostalCode(), data.getCountry(), data.getPaymentMethod(),
                        data.getErrorMessage())
                .getEmptyError(data.getErrorMessage());
        assertEquals(actualError.toUpperCase(), data.getErrorMessage().toUpperCase(), " invalid actual result and expected do not match");
    }
}
