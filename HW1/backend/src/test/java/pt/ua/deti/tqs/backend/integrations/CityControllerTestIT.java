package pt.ua.deti.tqs.backend.integrations;

import org.junit.jupiter.api.AfterEach;
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
import pt.ua.deti.tqs.backend.entities.City;
import pt.ua.deti.tqs.backend.repositories.CityRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class CityControllerTestIT {
    @LocalServerPort
    int randomServerPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CityRepository repository;

    @AfterEach
    public void resetDb() {
        repository.deleteAll();
    }

    @Test
    void whenValidInput_thenCreateCity() {
        City city = new City();
        city.setName("Aveiro");

        restTemplate.postForEntity("/api/city", city, City.class);

        List<City> found = repository.findAll();
        assertThat(found).extracting(City::getName).containsOnly(city.getName());
    }

    @Test
    void givenCities_whenGetCities_thenStatus200() {
        createTestCity("Aveiro");
        createTestCity("Porto");

        ResponseEntity<List<City>> response = restTemplate.exchange("/api/city", HttpMethod.GET, null,
                                                                    new ParameterizedTypeReference<>() {
                                                                    });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).extracting(City::getName).contains("Aveiro", "Porto");
    }

    @Test
    void whenGetCityById_thenStatus200() {
        City city = createTestCity("Aveiro");

        City found = restTemplate.getForObject("/api/city/" + city.getId(), City.class);
        assertThat(found.getName()).isEqualTo(city.getName());
    }

    @Test
    void whenGetCityByName_thenStatus200() {
        City city = createTestCity("Aveiro");

        ResponseEntity<List<City>> response =
                restTemplate.exchange("/api/city?name=" + city.getName(), HttpMethod.GET, null,
                                      new ParameterizedTypeReference<>() {
                                      });


        assertThat(response.getBody()).extracting(City::getName).contains(city.getName());
    }

    @Test
    void whenGetCityByInvalidId_thenStatus404() {
        ResponseEntity<City> response = restTemplate.getForEntity("/api/city/999", City.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void whenGetCityByInvalidName_thenStatus404() {
        ResponseEntity<List<City>> response =
                restTemplate.exchange("/api/city?name=Invalid", HttpMethod.GET, null,
                                      new ParameterizedTypeReference<>() {
                                      });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }

    private City createTestCity(String name) {
        City city = new City();
        city.setName(name);
        repository.saveAndFlush(city);
        return city;
    }
}
