package com.example.testing;
import com.example.page.*;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.testng.Assert.*;
public class RegisterTest extends BaseTest {
    @DataProvider(name = "registerErrorData")
    public Object[][] getRegisterErrorData()
    {
        return new Object[][]
        {
            {"","Tester","Test01@gmail.com","Admin@123","Admin@123","First Name must be at least 2 characters."},
            {"a","Tester","Test02@gmail.com","Admin@123","Admin@123","First Name must be at least 2 characters."},
            {"Test","Tester","","Admin@123","Admin@123","Please enter a valid email address."},
        };
    }
    @Test(dataProvider = "registerErrorData") // <-- This connects the DataProvider to the test
    public void TestRegisterErrors(String firstName, String lastName, String email , String password ,String confirmPassword, String ErrorsLog)
    {
        openUrl("/register");
        String actualError=new RegisterPage(driver).Register(firstName,lastName,email,password,confirmPassword).getTextError();
        assertEquals(actualError,ErrorsLog,"Not correct");
    }
}
