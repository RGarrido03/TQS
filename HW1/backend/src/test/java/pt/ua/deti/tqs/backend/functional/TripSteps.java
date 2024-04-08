package pt.ua.deti.tqs.backend.functional;

import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;

public class TripSteps {
    private WebDriver driver;

    @When("I navigate to {string}")
    public void iNavigateTo(String url) {
        driver = WebDriverManager.firefoxdriver().browserInDocker().create();
        driver.get(url);
    }
}
