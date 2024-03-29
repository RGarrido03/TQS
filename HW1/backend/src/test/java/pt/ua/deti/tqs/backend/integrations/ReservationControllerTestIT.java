package pt.ua.deti.tqs.backend.integrations;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import pt.ua.deti.tqs.backend.entities.*;
import pt.ua.deti.tqs.backend.repositories.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class ReservationControllerTestIT {
    @LocalServerPort
    int randomServerPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ReservationRepository repository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void resetDb() {
        repository.deleteAll();
        tripRepository.deleteAll();
        busRepository.deleteAll();
        cityRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void whenValidInput_thenCreateReservation() {
        Bus bus = new Bus();
        bus.setCapacity(50);
        bus = busRepository.saveAndFlush(bus);

        City city = new City();
        city.setName("Aveiro");
        city = cityRepository.saveAndFlush(city);

        User user = new User();
        user.setUsername("user");
        user.setEmail("user@ua.pt");
        user.setName("User");
        user.setPassword("password");
        user = userRepository.saveAndFlush(user);

        Trip trip = new Trip();
        trip.setBus(bus);
        trip.setPrice(10.0);
        trip.setDeparture(city);
        trip.setDepartureTime(LocalDateTime.now());
        trip.setArrival(city);
        trip.setArrivalTime(LocalDateTime.now().plusHours(1));
        trip.calculateFreeSeats();
        trip = tripRepository.saveAndFlush(trip);

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setTrip(trip);
        reservation.setPrice(10.0);
        reservation.setSeats(1);

        restTemplate.postForEntity("/api/reservation", reservation, Reservation.class);

        List<Reservation> found = repository.findAll();
        assertThat(found).extracting(Reservation::getPrice).containsOnly(reservation.getPrice());
        assertThat(found).extracting(Reservation::getSeats).containsOnly(reservation.getSeats());
        assertThat(found).extracting(Reservation::getUser).extracting(User::getUsername)
                         .containsOnly(reservation.getUser().getUsername());
        assertThat(found).extracting(Reservation::getTrip).extracting(Trip::getPrice)
                         .containsOnly(reservation.getTrip().getPrice());
        assertThat(found).extracting(Reservation::getTrip).extracting(Trip::getDeparture)
                         .extracting(City::getName).containsOnly(reservation.getTrip().getDeparture().getName());
        assertThat(found).extracting(Reservation::getTrip).extracting(Trip::getArrival)
                         .extracting(City::getName).containsOnly(reservation.getTrip().getArrival().getName());
        assertThat(found).extracting(Reservation::getTrip).extracting(Trip::getDepartureTime)
                         .containsOnly(reservation.getTrip().getDepartureTime());
        assertThat(found).extracting(Reservation::getTrip).extracting(Trip::getArrivalTime)
                         .containsOnly(reservation.getTrip().getArrivalTime());
    }

    @Test
    void givenReservations_whenGetReservations_thenStatus200() {
        Reservation reservation1 = createTestReservation();
        Reservation reservation2 = createTestReservation();

        List<Reservation> found = List.of(restTemplate.getForObject("/api/reservation", Reservation[].class));

        assertThat(found).extracting(Reservation::getPrice).contains(reservation1.getPrice(), reservation2.getPrice());
        assertThat(found).extracting(Reservation::getSeats).contains(reservation1.getSeats(), reservation2.getSeats());
        assertThat(found).extracting(Reservation::getUser).extracting(User::getUsername)
                         .contains(reservation1.getUser().getUsername(), reservation2.getUser().getUsername());
        assertThat(found).extracting(Reservation::getTrip).extracting(Trip::getPrice)
                         .contains(reservation1.getTrip().getPrice(), reservation2.getTrip().getPrice());
        assertThat(found).extracting(Reservation::getTrip).extracting(Trip::getDeparture)
                         .extracting(City::getName).contains(reservation1.getTrip().getDeparture().getName(),
                                                             reservation2.getTrip().getDeparture().getName());
        assertThat(found).extracting(Reservation::getTrip).extracting(Trip::getArrival)
                         .extracting(City::getName).contains(reservation1.getTrip().getArrival().getName(),
                                                             reservation2.getTrip().getArrival().getName());
        assertThat(found).extracting(Reservation::getTrip).extracting(Trip::getDepartureTime)
                         .contains(reservation1.getTrip().getDepartureTime(),
                                   reservation2.getTrip().getDepartureTime());
        assertThat(found).extracting(Reservation::getTrip).extracting(Trip::getArrivalTime)
                         .contains(reservation1.getTrip().getArrivalTime(), reservation2.getTrip().getArrivalTime());
    }

    @Test
    void whenGetReservationById_thenStatus200() {
        Reservation reservation = createTestReservation();

        Reservation found = restTemplate.getForObject("/api/reservation/" + reservation.getId(), Reservation.class);

        assertThat(found).isNotNull();
        assertThat(found.getPrice()).isEqualTo(reservation.getPrice());
        assertThat(found.getSeats()).isEqualTo(reservation.getSeats());
        assertThat(found.getUser().getUsername()).isEqualTo(reservation.getUser().getUsername());
        assertThat(found.getTrip().getPrice()).isEqualTo(reservation.getTrip().getPrice());
        assertThat(found.getTrip().getDeparture().getName()).isEqualTo(reservation.getTrip().getDeparture().getName());
        assertThat(found.getTrip().getArrival().getName()).isEqualTo(reservation.getTrip().getArrival().getName());
        assertThat(found.getTrip().getDepartureTime()).isEqualTo(reservation.getTrip().getDepartureTime());
        assertThat(found.getTrip().getArrivalTime()).isEqualTo(reservation.getTrip().getArrivalTime());
    }

    @Test
    void whenGetReservationByInvalidId_thenStatus404() {
        createTestReservation();

        Reservation found = restTemplate.getForObject("/api/reservation/999", Reservation.class);

        assertThat(found).isNull();
    }

    @Test
    void whenUpdateReservation_thenStatus200() {
        Reservation reservation = createTestReservation();
        reservation.setSeats(2);
        reservation.setPrice(20.0);

        restTemplate.put("/api/reservation/" + reservation.getId(), reservation);

        Reservation found = repository.findById(reservation.getId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getPrice()).isEqualTo(reservation.getPrice());
        assertThat(found.getSeats()).isEqualTo(reservation.getSeats());
        assertThat(found.getUser().getUsername()).isEqualTo(reservation.getUser().getUsername());
        assertThat(found.getTrip().getPrice()).isEqualTo(reservation.getTrip().getPrice());
        assertThat(found.getTrip().getDeparture().getName()).isEqualTo(reservation.getTrip().getDeparture().getName());
        assertThat(found.getTrip().getArrival().getName()).isEqualTo(reservation.getTrip().getArrival().getName());
        assertThat(found.getTrip().getDepartureTime()).isEqualTo(reservation.getTrip().getDepartureTime());
        assertThat(found.getTrip().getArrivalTime()).isEqualTo(reservation.getTrip().getArrivalTime());
    }

    @Test
    void whenDeleteReservation_thenStatus200() {
        Reservation reservation = createTestReservation();

        restTemplate.delete("/api/reservation/" + reservation.getId());

        Reservation found = repository.findById(reservation.getId()).orElse(null);
        assertThat(found).isNull();
    }

    private Reservation createTestReservation() {
        Bus bus = new Bus();
        bus.setCapacity(50);
        bus = busRepository.saveAndFlush(bus);

        City city = new City();
        city.setName("Aveiro");
        city = cityRepository.saveAndFlush(city);

        User user = new User();
        user.setEmail("user@ua.pt");
        user.setName("User");
        user.setUsername("user");
        user.setPassword("password");
        user = userRepository.saveAndFlush(user);

        Trip trip = new Trip();
        trip.setBus(bus);
        trip.setPrice(10.0);
        trip.setDeparture(city);
        trip.setDepartureTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        trip.setArrival(city);
        trip.setArrivalTime(LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.SECONDS));
        trip = tripRepository.saveAndFlush(trip);

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setTrip(trip);
        reservation.setPrice(10.0);
        reservation.setSeats(1);

        return repository.saveAndFlush(reservation);
    }
}
