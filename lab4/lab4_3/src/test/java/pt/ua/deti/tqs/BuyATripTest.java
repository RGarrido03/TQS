package pt.ua.deti.tqs;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SeleniumJupiter.class)
public class BuyATripTest {
    // Use @DockerBrowser(type = BrowserType.FIREFOX) for using a containerized browser.
    @Test
    public void buyatrip(FirefoxDriver driver) {
        driver.get("https://blazedemo.com/");

        // Main page
        MainPage mainPage = new MainPage(driver);
        mainPage.setDepartureSelect();
        mainPage.setDestinationSelect();
        mainPage.clickFindButton();

        // Flight page
        FlightPage flightPage = new FlightPage(driver);
        flightPage.chooseFlight();

        // Reservation page
        ReservationPage reservationPage = new ReservationPage(driver);
        reservationPage.clickNameInput();
        reservationPage.setNameInput("Rúben Garrido");
        reservationPage.submitForm();

        // Confirmation page
        ConfirmationPage confirmationPage = new ConfirmationPage(driver);
        assertThat(confirmationPage.getTitle()).isEqualTo("BlazeDemo Confirmation");
    }
}
