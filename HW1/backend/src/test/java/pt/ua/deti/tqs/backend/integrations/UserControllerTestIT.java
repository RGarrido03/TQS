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
import pt.ua.deti.tqs.backend.entities.*;
import pt.ua.deti.tqs.backend.repositories.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class UserControllerTestIT {
    @LocalServerPort
    int randomServerPort;

    @Autowired
    private TestRestTemplate restTemplate;

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

    @AfterEach
    public void resetDb() {
        repository.deleteAll();
    }

    @Test
    void whenGetUserById_thenStatus200() {
        User user = createTestUser("John Doe", "johndoe@ua.pt", "johndoe", "password");

        ResponseEntity<User> response = restTemplate.getForEntity("/api/user/" + user.getId(), User.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).extracting(User::getName).isEqualTo(user.getName());
    }

    @Test
    void whenGetUserByInvalidId_thenStatus404() {
        ResponseEntity<User> response = restTemplate.getForEntity("/api/user/999", User.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void whenGetUserReservationsByUserId_thenStatus200() {
        User user = createTestUser("Jane Doe", "janedoe@ua.pt", "janedoe", "password");
        user = repository.saveAndFlush(user);
        Reservation reservation = createTestReservation(user);

        ResponseEntity<List<Reservation>> response =
                restTemplate.exchange("/api/user/" + user.getId() + "/reservations", HttpMethod.GET, null,
                                      new ParameterizedTypeReference<>() {
                                      });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1).extracting(Reservation::getId).contains(reservation.getId());
    }

    @Test
    void whenDeleteUser_thenStatus200() {
        User user = createTestUser("John Doe", "johndoe@ua.pt", "johndoe", "password");

        restTemplate.delete("/api/user/" + user.getId());

        assertThat(repository.findById(user.getId())).isEmpty();
    }

    private User createTestUser(String name, String email, String username, String password) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        repository.saveAndFlush(user);
        return user;
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

        reservationRepository.saveAndFlush(reservation);
        return reservation;
    }
}
