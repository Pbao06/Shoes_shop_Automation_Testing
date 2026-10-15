package com.example.testing;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
public class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;
    
    protected String urlbase = "https://shoes-shop-bice.vercel.app/";

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless=new");
        // options.addArguments("--no-sandbox");
        // options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);

        driver.manage().window().maximize();
        wait = new WebDriverWait(
            driver,
            Duration.ofSeconds(50)
        );
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
    public void openUrl(String chunks)
    {
        driver.get(urlbase+ chunks);
    }
}
