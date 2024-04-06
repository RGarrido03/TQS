package pt.ua.deti.tqs.backend.integrations;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import pt.ua.deti.tqs.backend.entities.Bus;
import pt.ua.deti.tqs.backend.repositories.BusRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase
class BusControllerTestIT {
    String BASE_URL;

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private BusRepository repository;

    @BeforeAll
    void setBASE_URL() {
        BASE_URL = "http://localhost:" + randomServerPort;
    }

    @AfterEach
    public void resetDb() {
        repository.deleteAll();
    }

    @Test
    void whenValidInput_thenCreateBus() {
        Bus bus = new Bus();
        bus.setCapacity(50);

        RestAssured.given().contentType(ContentType.JSON).body(bus)
                   .when().post(BASE_URL + "/api/bus")
                   .then().statusCode(HttpStatus.CREATED.value())
                   .body("capacity", equalTo(bus.getCapacity()));

        List<Bus> found = repository.findAll();
        assertThat(found).extracting(Bus::getCapacity).containsOnly(bus.getCapacity());
    }

    @Test
    void givenBuses_whenGetBuses_thenStatus200() {
        Bus bus1 = createTestBus(50);
        Bus bus2 = createTestBus(60);

        RestAssured.when().get(BASE_URL + "/api/bus")
                   .then().statusCode(HttpStatus.OK.value())
                   .body("", hasSize(2))
                   .body("capacity", hasItems(bus1.getCapacity(), bus2.getCapacity()));
    }

    @Test
    void whenGetBusById_thenStatus200() {
        Bus bus = createTestBus(50);

        RestAssured.when().get(BASE_URL + "/api/bus/" + bus.getId())
                   .then().statusCode(HttpStatus.OK.value())
                   .body("capacity", equalTo(bus.getCapacity()));
    }

    @Test
    void whenGetBusByInvalidId_thenStatus404() {
        RestAssured.when().get(BASE_URL + "/api/bus/999")
                   .then().statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void whenUpdateBus_thenStatus200() {
        Bus bus = createTestBus(50);

        bus.setCapacity(60);
        RestAssured.given().contentType(ContentType.JSON).body(bus)
                   .when().put(BASE_URL + "/api/bus/" + bus.getId())
                   .then().statusCode(HttpStatus.OK.value())
                   .body("capacity", equalTo(bus.getCapacity()));

        Bus updatedBus = repository.findById(bus.getId()).orElse(null);
        assertThat(updatedBus).isNotNull().extracting(Bus::getCapacity).isEqualTo(60);
    }

    @Test
    void whenDeleteBus_thenStatus200() {
        Bus bus = createTestBus(50);

        RestAssured.when().delete(BASE_URL + "/api/bus/" + bus.getId())
                   .then().statusCode(HttpStatus.OK.value());

        assertThat(repository.findById(bus.getId())).isEmpty();
    }

    private Bus createTestBus(int capacity) {
        Bus bus = new Bus();
        bus.setCapacity(capacity);
        repository.saveAndFlush(bus);
        return bus;
    }
}
