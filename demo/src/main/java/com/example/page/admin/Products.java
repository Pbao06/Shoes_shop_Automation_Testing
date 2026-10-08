package com.example.page.admin;
import com.example.page.*;
import com.example.models.admin.*;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.By;

public class Products extends BasePage {

    private By inputSearch = By.cssSelector("input[placeholder='Search products...']");
    // specific product/id/2
    private By valueName = By.xpath("//tr[@data-testid='product-row-2']/td[count(//th[contains(normalize-space(),'Product')]/preceding-sibling::th)+1] //p[1]");
    private By valueSlug = By.xpath("//tr[@data-testid='product-row-2']/td[count(//th[contains(normalize-space(),'Product')]/preceding-sibling::th)+1] //p[2]");
    private By valueBrand = By.xpath("//tr[@data-testid='product-row-2']/td[     count(//th[contains(normalize-space(),'Brand')]/preceding-sibling::th) + 1 ]");
    private By valueCate = By.xpath("//tr[@data-testid='product-row-2']/td[count(//th[contains(normalize-space(),'Category')]/preceding-sibling::th)+1]");
    private By valueSalePrice = By.xpath("//tr[@data-testid='product-row-2']/td[count(//th[contains(normalize-space(),'Price')]/preceding-sibling::th)+1] //span[1]");
    private By valuePrice = By.xpath("//tr[@data-testid='product-row-2']/td[count(//th[contains(normalize-space(),'Price')]/preceding-sibling::th)+1] //span[2]");
    private By iconEdit = By.xpath("//a[@href='/admin/products/2/edit']");

    public Products(WebDriver driver) {
        super(driver);
    }

    public Products SearchData() {
        waitForVisible(inputSearch).sendKeys("Classic");
        return this;
    }

    public String getNameProduct() {
        return waitForVisible(valueName).getText();
    }

    public ProductEdit clickProductEdit() {
        waitForClickable(iconEdit).click();
        wait.until(ExpectedConditions.urlContains("/products/2/edit"));
        return new ProductEdit(driver);
    }

    private String formatCurrency(double amount) {
        return String.format("$%.2f", amount);
    }

    public boolean isEditSuccess(ProductEditClass data) {
    // 1. Lấy và chuẩn hóa dữ liệu từ UI (chuyển hết về lower case & trim)
    String actualName = waitForVisible(valueName).getText().trim().toLowerCase();
    String actualSlug = waitForVisible(valueSlug).getText().trim().toLowerCase();
    String actualBrand = waitForVisible(valueBrand).getText().trim().toLowerCase();
    String actualCate = waitForVisible(valueCate).getText().trim().toLowerCase();
    
    String actualPrice = waitForVisible(valuePrice).getText().trim();
    String actualSalePrice = waitForVisible(valueSalePrice).getText().trim();

    // 2. Chuẩn hóa dữ liệu Parameter đầu vào (chuyển về lower case & format giá tiền)
    String expectedName = data.getProductName().trim().toLowerCase();
    String expectedSlug = data.getSlug().trim().toLowerCase();
    String expectedBrand = data.getBrand().trim().toLowerCase();
    String expectedCategory = data.getCategory().trim().toLowerCase();

    String expectedPrice = formatCurrency(data.getPrice());
    String expectedSalePrice = formatCurrency(data.getSalePrice());

    // 3. Kiểm tra từng trường
    if (!actualName.contains(expectedName)) {
        System.err.printf("❌ NAME LỆCH | Expected: '%s' | Actual UI: '%s'%n", expectedName, actualName);
        return false;
    }

    if (!actualSlug.contains(expectedSlug)) {
        System.err.printf("❌ SLUG LỆCH | Expected: '%s' | Actual UI: '%s'%n", expectedSlug, actualSlug);
        return false;
    }

    if (!actualBrand.contains(expectedBrand)) {
        System.err.printf("❌ BRAND LỆCH | Expected: '%s' | Actual UI: '%s'%n", expectedBrand, actualBrand);
        return false;
    }

    if (!actualCate.contains(expectedCategory)) {
        System.err.printf("❌ CATEGORY LỆCH | Expected: '%s' | Actual UI: '%s'%n", expectedCategory, actualCate);
        return false;
    }

    if (!actualPrice.contains(expectedPrice)) {
        System.err.printf("❌ PRICE LỆCH | Expected: '%s' | Actual UI: '%s'%n", expectedPrice, actualPrice);
        return false;
    }

    if (!actualSalePrice.contains(expectedSalePrice)) {
        System.err.printf("❌ SALE PRICE LỆCH | Expected: '%s' | Actual UI: '%s'%n", expectedSalePrice, actualSalePrice);
        return false;
    }

    return true;
}
}
