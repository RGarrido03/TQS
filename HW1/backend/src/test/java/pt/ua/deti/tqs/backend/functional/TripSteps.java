package pt.ua.deti.tqs.backend.functional;

import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class TripSteps {
    private WebDriver driver;

    @When("I navigate to {string}")
    public void iNavigateTo(String url) {
        FirefoxBinary binary = new FirefoxBinary();
        binary.addCommandLineOptions("-headless");
        FirefoxOptions options = new FirefoxOptions();
        options.setBinary(binary);

        driver = new FirefoxDriver(options);
        driver.get(url);
    }
}
