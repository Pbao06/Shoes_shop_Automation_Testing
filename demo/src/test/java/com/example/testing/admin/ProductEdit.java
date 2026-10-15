package com.example.testing.admin;
import com.example.testing.*;
import com.example.page.*;
import com.example.models.admin.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import static org.testng.Assert.*;

public class ProductEdit extends BaseTest{

    // Brand scope valid: Atelier,  Nova
    // Cate scope valid : Shoes, Bags, Accessories
    // Status scope valid : Active , Inactive


    @DataProvider(name="DataProductEdit")
    public Object[][] getDataProductEdit()
    {
        return new  Object[][] 
        {
          {
                new ProductEditClass(
                    "Test 1",
                    "phan-bao",
                    "Test Description",
                    450,
                    440, // sale price must not exceed regular price 
                    "Black,White",
                    "Nova",
                    "Bags",
                    "Active",
                    "c:\\Users\\phang\\Downloads\\banner.jpeg"
                )
            }  
        };
    }
    @Test(dataProvider="DataProductEdit")
    public void HappyPath(ProductEditClass data) {
    openUrl("/login");

    boolean edited = new LoginPage(driver)
            .typeLogin("admin@example.com", "Admin@123")
            .clickAdmin()
            .clickProducts()
            .clickProductEdit()
            .EditProduct(data)     // Truyền thẳng data
            .isEditSuccess(data);  // Truyền thẳng data

        assertTrue(edited, "Test case fail - Edit Product validation failed");
    }
}