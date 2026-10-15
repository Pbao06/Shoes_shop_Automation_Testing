package com.example.page;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.By;

import org.openqa.selenium.support.ui.ExpectedConditions;
import com.example.models.*;
import org.openqa.selenium.support.ui.WebDriverWait;
public class OrderCustomerPage extends BasePage
{

    // product id =2 
    private By nameProduct=By.xpath("//article[@data-testid='order-card-1']//h3[@data-testid='order-item-name-1-2']");
    private By orderItemMeta=By.xpath("//article[@data-testid='order-item-meta-1-2']");
    private By statusProduct=By.xpath("//p[@data-testid='order-status-1']");
    private By totalPrice=By.xpath("//p[@data-testid='order-total-1']");
    private By allTab=By.cssSelector("button[data-testid='filter-all']");
    private By pendingTab=By.cssSelector("button[data-testid='filter-pending']");
    private By processingTab=By.cssSelector("button[data-testid='filter-processing']");
    private By shippedTab=By.cssSelector("button[data-testid='filter-shipped']");
    private By deliveredTab=By.cssSelector("button[data-testid='filter-delivered']");
    private By cancelledTab=By.cssSelector("button[data-testid='filter-cancelled']");
    // order empty error when no any product have in tab 
    private By errorTab=By.cssSelector("p[data-testid='orders-empty']");
     public String getOrderItemMeta() {
        return driver.findElement(orderItemMeta).getText();
    }
    public String getSize() {
    return getOrderItemMeta()
            .split("·")[0]
            .replace("Size:", "").trim();
    }

    public int getQuantity() {
        return Integer.parseInt(
            getOrderItemMeta()
                .split("·")[1]
                .replace("Qty:", "").trim()
        );
    }
    // when we check 
    public OrderCustomerPage(WebDriver driver)
    { 
        super(driver);
    }

    public boolean checkOtherTabError()
    {
        String status=waitForVisible(statusProduct).getText().trim().toLowerCase();
        String pending=waitForVisible(pendingTab).getText().trim().toLowerCase();
        String processing=waitForVisible(processingTab).getText().trim().toLowerCase();
        String shipped=waitForVisible(shippedTab).getText().trim().toLowerCase();
        String delivered=waitForVisible(deliveredTab).getText().trim().toLowerCase();
        String cancelled=waitForVisible(cancelledTab).getText().trim().toLowerCase();
        if(!status.equals(pending))
        {
            waitForVisible(pendingTab).click();
            String error = waitForVisible(errorTab)
                    .getText()
                    .trim()
                    .toLowerCase();

            if (!error.equals("no orders in this category.")) {
                return false;
            }
        }
        if(!status.equals(processing))
        {
            waitForVisible(processingTab).click();
            String error = waitForVisible(errorTab)
                    .getText()
                    .trim()
                    .toLowerCase();

            if (!error.equals("no orders in this category.")) {
                return false;
            }


        }
        if(!status.equals(shipped))
        {
            waitForVisible(shippedTab).click();
            String error = waitForVisible(errorTab)
                    .getText()
                    .trim()
                    .toLowerCase();

            if (!error.equals("no orders in this category.")) {
                return false;
            }
        }
        if(!status.equals(delivered))
        {
            waitForVisible(deliveredTab).click();
            String error = waitForVisible(errorTab)
                    .getText()
                    .trim()
                    .toLowerCase();

            if (!error.equals("no orders in this category.")) {
                return false;
            }
        }
        if(!status.equals(cancelled))
        {
            waitForVisible(cancelledTab).click();
            String error = waitForVisible(errorTab)
                    .getText()
                    .trim()
                    .toLowerCase();

            if (!error.equals("no orders in this category.")) {
                return false;
            }
        }
        return true;
    }
    public boolean checkinformationProduct(CheckoutData checkout )
    {
        if(!waitForVisible(statusProduct).getText().toLowerCase().equals("pending".toLowerCase())) return false;
        if(getSize() != checkout.getSize()) return false;
        if(getQuantity() != checkout.getQuantity()) return false;
            String actualName = waitForVisible(nameProduct).getText().trim().toLowerCase();
            String expectedName = checkout.getName().trim().toLowerCase();
        if(actualName != expectedName) return false;
        if(!waitForVisible(totalPrice).getText().trim().equals(String.valueOf(checkout.getTotalPrice()).trim())) return false;
        return true;
    }
}