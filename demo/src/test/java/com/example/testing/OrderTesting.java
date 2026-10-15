package com.example.testing;

import com.example.page.*;
import com.example.models.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.testng.Assert.*;


public class OrderTesting extends BaseTest
{
    @DataProvider(name = "CheckoutValidData")
    public Object[][] getRegisterErrorData()
     {
        return new Object[][] {
                { new Checkout("Valid", "Phan", "Bao@example.com", " Phan Dinh Phung Q1 district", "Ho Chi Minh", "232",
                        "United Kingdom", "PayPal",null) 
                 }
         };
    }
    @Test(dataProvider = "CheckoutValidData")
    public void order_Test_Tab(Checkout data)
    {
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
         var checkoutPage=productDetail.clickCart()
                .clickCheckout();
        CheckoutData checkout=checkoutPage.getInfoProduct();

        var order_check_othertab=checkoutPage.TypeInfomation(data.getFirstName(), data.getLastName(), data.getEmail(), data.getAddress(),
                data.getCity(), data.getPostalCode(), data.getCountry(), data.getPaymentMethod(),
                data.getErrorMessage()).clickOrderPage().checkOtherTabError();
        assertTrue(order_check_othertab," lol wrong");
    }

//     @Test(dataProvider = "CheckoutValidData") // <-- This connects the DataProvider to the test
//     public void Order_Smoke_test(Checkout data) {

//         openUrl("/login"); // Navigate to base URL + /login

//         ProductDetail productDetail = new LoginPage(driver)
//                 .typeLogin("admin@example.com", "Admin@123")
//                 .clickProductDetail()
//                 .clickChooseSize()
//                 .clickAddToCart();

//         // 1. Capture initial badge count
//         int initialCount = productDetail.getCartBadgeCount();

//         // 2. Add to cart again and verify badge increments
//         productDetail.clickAddToCart()
//                 .verifyBadgeIncremented(initialCount);

//         // 3. Navigate to Cart -> Checkout -> Fill order info & Submit
//          var checkoutPage=productDetail.clickCart()
//                 .clickCheckout();
//         CheckoutData checkout=checkoutPage.getInfoProduct();

//         var order_check_smoke=checkoutPage.TypeInfomation(data.getFirstName(), data.getLastName(), data.getEmail(), data.getAddress(),
//                 data.getCity(), data.getPostalCode(), data.getCountry(), data.getPaymentMethod(),
//                 data.getErrorMessage()).clickOrderPage().checkinformationProduct(checkout);
//         assertTrue(order_check_smoke," lol wrong");
//     }
}