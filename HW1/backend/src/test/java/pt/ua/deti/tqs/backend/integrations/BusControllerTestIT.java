package pt.ua.deti.tqs.backend.integrations;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pt.ua.deti.tqs.backend.entities.Bus;
import pt.ua.deti.tqs.backend.repositories.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class BusControllerTestIT {
    @LocalServerPort
    int randomServerPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BusRepository repository;

    @AfterEach
    public void resetDb() {
        repository.deleteAll();
    }

    @Test
    void whenValidInput_thenCreateBus() {
        Bus bus = new Bus();
        bus.setCapacity(50);

        restTemplate.postForEntity("/api/bus", bus, Bus.class);

        List<Bus> found = repository.findAll();
        assertThat(found).extracting(Bus::getCapacity).containsOnly(bus.getCapacity());
    }

    @Test
    void givenBuses_whenGetBuses_thenStatus200() {
        Bus bus1 = createTestBus(50);
        Bus bus2 = createTestBus(60);

        ResponseEntity<List<Bus>> response =
                restTemplate.exchange("/api/bus", HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).extracting(Bus::getCapacity).contains(bus1.getCapacity(), bus2.getCapacity());
    }

    @Test
    void whenGetBusById_thenStatus200() {
        Bus bus = createTestBus(50);

        ResponseEntity<Bus> response = restTemplate.getForEntity("/api/bus/" + bus.getId(), Bus.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull().extracting(Bus::getCapacity).isEqualTo(bus.getCapacity());
    }

    @Test
    void whenGetBusByInvalidId_thenStatus404() {
        ResponseEntity<Bus> response = restTemplate.getForEntity("/api/bus/999", Bus.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void whenUpdateBus_thenStatus200() {
        Bus bus = createTestBus(50);

        bus.setCapacity(60);
        restTemplate.put("/api/bus/" + bus.getId(), bus);

        Bus updatedBus = repository.findById(bus.getId()).orElse(null);
        assertThat(updatedBus).isNotNull().extracting(Bus::getCapacity).isEqualTo(60);
    }

    @Test
    void whenDeleteBus_thenStatus200() {
        Bus bus = createTestBus(50);

        restTemplate.delete("/api/bus/" + bus.getId());

        assertThat(repository.findById(bus.getId())).isEmpty();
    }

    private Bus createTestBus(int capacity) {
        Bus bus = new Bus();
        bus.setCapacity(capacity);
        repository.saveAndFlush(bus);
        return bus;
    }
}
