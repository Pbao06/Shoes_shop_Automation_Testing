package com.example.page;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SearchPage extends BasePage{
    private By inputSearch = By.cssSelector("input[placeholder='Search products...']");
    private By product_id3=By.cssSelector("a[href='/product/3']");
        public SearchPage(WebDriver driver)
        {
            super(driver);
        }
    public void clickProductFound()
    {
        waitForVisible(product_id3).click(); // explicity waits here 
    }
    public void sendDataToLink(String value )
    { 
         TypeInput(inputSearch,value);

    }

}
