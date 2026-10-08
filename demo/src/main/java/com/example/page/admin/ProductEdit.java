package com.example.page.admin;
import com.example.page.*;

 import org.openqa.selenium.WebDriver;

import java.time.Duration;
import com.example.models.admin.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
public class ProductEdit extends BasePage {

    private By ip_ProductName = By.xpath("//label[span[contains(text(), 'Product Name')]]//input");
    private By ip_Slug=By.xpath("//label[span[contains(text(), 'Slug')]]//input");
    private By ip_Desciption=By.xpath("//label[span[contains(text(), 'Description')]]//textarea"); 
    private By ip_Price=By.xpath("//label[span[contains(text(),'Price')]]//input");
    private By ip_SalePrice=By.xpath("//label[span[contains(text(),'Sale Price')]]//input");
    private By ip_Color= By.xpath("//label[span[contains(text(),'Color')]]//input");

    private By select_Brand=By.xpath("//label[span[contains(text(),'Brand')]]//select");
    private String option_Brand="//label[span[contains(text(),'Brand')]]//select //option[normalize-space()='value']";




    private By select_Cate=By.xpath("//label[span[contains(text(),'Category')]]//select");
    private String option_Cate="//label[span[contains(text(),'Category')]] //select //option[normalize-space()='value']";




    private By select_Status=By.xpath("//label[span[contains(text(),'Status')]]//select");



    // img attach file 
    private By btn_File_Img=By.cssSelector("input[type='file']");
    private By img_Banner=By.cssSelector("img[alt='banner']");

    // button -> save product 
    private By btn_Save_Product= By.xpath("//button[normalize-space()='Save Product']");
    public ProductEdit(WebDriver driver)
    {
        super(driver);
    }
    public Products EditProduct(ProductEditClass data) {
        visiableClearThenSendData(ip_ProductName, data.getProductName());
        visiableClearThenSendData(ip_Slug, data.getSlug());
        visiableClearThenSendData(ip_Desciption, data.getDescription());
        
        // Tự động chuyển double/number sang String để sendKeys
        visiableClearThenSendData(ip_Price, String.valueOf(data.getPrice()));
        visiableClearThenSendData(ip_SalePrice, String.valueOf(data.getSalePrice()));
        
        clickSelectBrand(data.getBrand());
        clickSelectCategory(data.getCategory());
        uploadBannerImage(data.getImg());

        driver.findElement(btn_Save_Product).click();

        // instead of waiting for url we wait element 
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tr[@data-testid='product-row-2']")));
        
        return new Products(driver);
    }
    public void clickSelectBrand(String value)
    {
         String dynamicOption=option_Brand.replace("value",value);
        By elementOption=By.xpath(dynamicOption);
        waitForVisible(select_Brand).click();
       
        driver.findElement(elementOption).click();
    }
    public void clickSelectCategory(String value)
    {
         String dynamicOption=option_Cate.replace("value",value);
        By elementOption=By.xpath(dynamicOption);
        waitForVisible(select_Cate).click();
        driver.findElement(elementOption).click();
    }
    public void clickSelectStatus(String value)
    {
        String staticOption="//label[span[contains(text(),'Status')]] //select //option[normalize-space()='value']";
        String DynamicOption=staticOption.replace("value",value);
        By addressOptionStatus= By.xpath(DynamicOption);
        waitForVisible(select_Status).click();
        driver.findElement(addressOptionStatus).click();
    }
    public void  uploadBannerImage(String filePath) {
        // Use the BasePage function directly!
        uploadFileAndWait(btn_File_Img, filePath, img_Banner);
    }

    public boolean isDisplayImg()
    {
       return  driver.findElement(img_Banner).isDisplayed();
    }
}
