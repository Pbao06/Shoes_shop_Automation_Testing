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
                 { new Checkout("Nguyễn Thị Hoài ", "Phan", "abcde@domain.com",
                  "", "Ho Chi Minh", "232",
                 "United Kingdom", "PayPal", "This field is required.") }, // address empty
                 { new Checkout("Nguyễn Thị Hoài ", "Phan", "abcde@domain.com",
                  " Phan r", "Ho Chi Minh", "232",
                 "United Kingdom", "PayPal", "Address must be at least 5 characters.") }, // address < 5 ,
                 { new Checkout("Nguyễn Thị Hoài ", "Phan", "abcde@domain.com",
                  "A Narrative Essay is a type of writing that asks you to tell a personal story. Unlike other essay types, Narrative Essays are not strictly bound by academic structure. So how can you write an effective and impressive Narrative Essay? In this article, IDP IELTS will guide you through how to craft a compelling Narrative Essay step by step.", "Ho Chi Minh", "232",
                 "United Kingdom", "PayPal", "Address must be at most 200 characters.") }, // address >200 
                 { new Checkout("Nguyễn Thị Hoài ", "Phan", "abcde@domain.com",
                  " PHan Dnih giot ", "Ho Chi Minh", "232",
                 "United Kingdom", "PayPal", null) }, // address in valid scope
                 { new Checkout("Test01 ", "Phan", "abcde@domain.com",
                  " PHan Dnih giot ", "", "232",
                 "United Kingdom", "PayPal","This field is required") }, // city empty 
                 { new Checkout("Test01 ", "Phan", "abcde@domain.com",
                  " PHan Dnih giot ", "s", "232",
                 "United Kingdom", "PayPal","City must be at least 2 characters.") } ,// the length citiy <2
                 { new Checkout("Test01 ", "Phan", "abcde@domain.com",
                  " PHan Dnih giot ", "A Narrative Essay is a type of writing that asks you to tell a personal story. Unlike other essay types", "232",
                 "United Kingdom", "PayPal","City must be at most 100 characters") } ,// the length citiy >100
                 { new Checkout("Test01 ", "Phan", "abcde@domain.com",
                  " PHan Dinh giot ", "Phan$#@", "232",
                 "United Kingdom", "PayPal","City may only contain letters, spaces, hyphens, apostrophes, and dots.") } ,// valid scope but -> use strange words
                 { new Checkout("Test01 ", "Phan", "abcde@domain.com",
                  " PHan Dinh giot ", "Ho Chi MINH", "",
                 "United Kingdom", "PayPal","This field is required.") }, // empty => value postalcode 
                  { new Checkout("Test01 ", "Phan", "abcde@domain.com",
                  " PHan Dinh giot ", "Ho Chi MINH", "33",
                 "United Kingdom", "PayPal","Postal code must be at least 3 characters.") }, //value postalcode<3
                 { new Checkout("Test01 ", "Phan", "abcde@domain.com",
                  " PHan Dinh giot ", "Ho Chi MINH", "336767676767444442345676776",
                 "United Kingdom", "PayPal","Postal code must be at most 20 characters.") }, // postal code > 20
                  { new Checkout("Test01 ", "Phan", "abcde@domain.com",
                  " PHan Dinh giot ", "Ho Chi MINH", "45466@#$#",
                 "United Kingdom", "PayPal","Postal code may only contain letters, numbers, spaces, hyphens, dots, and hash signs.") }, // strange character postal code in valid scope 




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
