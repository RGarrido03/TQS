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
import pt.ua.deti.tqs.backend.entities.*;
import pt.ua.deti.tqs.backend.repositories.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase
class UserControllerTestIT {
    String BASE_URL;

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private UserRepository repository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private CityRepository cityRepository;

    @BeforeAll
    void setBASE_URL() {
        BASE_URL = "http://localhost:" + randomServerPort;
    }

    @AfterEach
    public void resetDb() {
        reservationRepository.deleteAll();
        tripRepository.deleteAll();
        busRepository.deleteAll();
        cityRepository.deleteAll();
        repository.deleteAll();
    }

    @Test
    void whenValidInput_thenCreateUser() {
        User user = new User();
        user.setUsername("johndoe");
        user.setPassword("password");
        user.setName("John Doe");
        user.setEmail("johndoe@ua.pt");

        RestAssured.given().contentType(ContentType.JSON)
                   .body("{\"username\":\"johndoe\",\"password\":\"password\",\"name\":\"John Doe\",\"email\":\"johndoe@ua.pt\"}")
                   .when().post(BASE_URL + "/api/user")
                   .then().statusCode(HttpStatus.CREATED.value())
                   .body("username", equalTo(user.getUsername()))
                   .body("name", equalTo(user.getName()))
                   .body("email", equalTo(user.getEmail()));

        List<User> found = repository.findAll();
        assertThat(found).extracting(User::getUsername).containsOnly(user.getUsername());
        assertThat(found).extracting(User::getName).containsOnly(user.getName());
        assertThat(found).extracting(User::getEmail).containsOnly(user.getEmail());
    }

    @Test
    void whenGetUserById_thenStatus200() {
        User user = createTestUser("John Doe", "johndoe@ua.pt", "johndoe", "password");

        RestAssured.when().get(BASE_URL + "/api/user/" + user.getId())
                   .then().statusCode(HttpStatus.OK.value())
                   .body("name", equalTo(user.getName()))
                   .body("email", equalTo(user.getEmail()))
                   .body("username", equalTo(user.getUsername()));
    }

    @Test
    void whenGetUserByInvalidId_thenStatus404() {
        RestAssured.when().get(BASE_URL + "/api/user/999")
                   .then().statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void whenGetUserReservationsByUserId_thenStatus200() {
        User user = createTestUser("Jane Doe", "janedoe@ua.pt", "janedoe", "password");
        Reservation reservation = createTestReservation(user);

        RestAssured.when().get(BASE_URL + "/api/user/" + user.getId() + "/reservations")
                   .then().statusCode(HttpStatus.OK.value())
                   .body("", hasSize(1))
                   .body("id", hasItems(reservation.getId().intValue()));
    }

    @Test
    void whenUpdateUser_thenStatus200() {
        User user = createTestUser("John Doe", "joghndoe@ua.pt", "johndoe", "password");

        user.setName("Jane Doe");
        RestAssured.given().contentType(ContentType.JSON)
                   .body("{\"name\":\"Jane Doe\", \"email\":\"johndoe@ua.pt\", \"username\":\"johndoe\", \"password\":\"password\"}")
                   .when().put(BASE_URL + "/api/user/" + user.getId())
                   .then().statusCode(HttpStatus.OK.value()).body("name", equalTo(user.getName()));

        User updated = repository.findById(user.getId()).orElse(null);
        assertThat(updated).isNotNull().extracting(User::getName).isEqualTo(user.getName());
    }

    @Test
    void whenUpdateInvalidUser_thenStatus404() {
        User user = createTestUser("John Doe", "johndoe@ua.pt", "johndoe", "password");

        RestAssured.given().contentType(ContentType.JSON).body(user)
                   .when().put(BASE_URL + "/api/user/999")
                   .then().statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void whenDeleteUser_thenStatus200() {
        User user = createTestUser("John Doe", "johndoe@ua.pt", "johndoe", "password");

        RestAssured.when().delete(BASE_URL + "/api/user/" + user.getId())
                   .then().statusCode(HttpStatus.OK.value());

        assertThat(repository.findById(user.getId())).isEmpty();
    }

    private User createTestUser(String name, String email, String username, String password) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        return repository.saveAndFlush(user);
    }

    private Reservation createTestReservation(User user) {
        Bus bus = new Bus();
        bus.setCapacity(50);
        bus = busRepository.saveAndFlush(bus);

        City city = new City();
        city.setName("Aveiro");
        city = cityRepository.saveAndFlush(city);

        Trip trip = new Trip();
        trip.setBus(bus);
        trip.setPrice(10.0);
        trip.setDeparture(city);
        trip.setDepartureTime(LocalDateTime.now());
        trip.setArrival(city);
        trip.setArrivalTime(LocalDateTime.now().plusHours(1));
        trip = tripRepository.saveAndFlush(trip);

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setTrip(trip);
        reservation.setPrice(10.0);
        reservation.setSeats(1);

        return reservationRepository.saveAndFlush(reservation);
    }
}
