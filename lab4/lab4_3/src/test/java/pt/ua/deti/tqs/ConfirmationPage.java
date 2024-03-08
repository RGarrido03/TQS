package pt.ua.deti.tqs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

// page_url = https://blazedemo.com/confirmation.php
public class ConfirmationPage {
    private final WebDriver driver;

    public ConfirmationPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public String getTitle() {
        return driver.getTitle();
    }
}