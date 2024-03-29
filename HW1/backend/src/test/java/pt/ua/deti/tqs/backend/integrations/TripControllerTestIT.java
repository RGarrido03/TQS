package pt.ua.deti.tqs.backend.integrations;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import pt.ua.deti.tqs.backend.entities.Bus;
import pt.ua.deti.tqs.backend.entities.City;
import pt.ua.deti.tqs.backend.entities.Trip;
import pt.ua.deti.tqs.backend.repositories.BusRepository;
import pt.ua.deti.tqs.backend.repositories.CityRepository;
import pt.ua.deti.tqs.backend.repositories.TripRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class TripControllerTestIT {
    @LocalServerPort
    int randomServerPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TripRepository repository;

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private CityRepository cityRepository;

    @AfterEach
    public void resetDb() {
        repository.deleteAll();
    }

    @Test
    void whenValidInput_thenCreateTrip() {
        Bus bus = new Bus();
        bus.setCapacity(50);
        bus = busRepository.saveAndFlush(bus);

        City city = new City();
        city.setName("Aveiro");
        city = cityRepository.saveAndFlush(city);

        Trip trip = new Trip();
        trip.setDeparture(city);
        trip.setDepartureTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        trip.setArrival(city);
        trip.setArrivalTime(LocalDateTime.now().plusHours(3).truncatedTo(ChronoUnit.SECONDS));
        trip.setBus(bus);
        trip.setPrice(10.0);

        restTemplate.postForEntity("/api/trip", trip, Trip.class);

        List<Trip> found = repository.findAll();
        assertThat(found).extracting(Trip::getPrice).containsOnly(trip.getPrice());
        assertThat(found).extracting(Trip::getDeparture).extracting(City::getName)
                         .containsOnly(trip.getDeparture().getName());
        assertThat(found).extracting(Trip::getArrival).extracting(City::getName)
                         .containsOnly(trip.getArrival().getName());
        assertThat(found).extracting(Trip::getBus).extracting(Bus::getCapacity)
                         .containsOnly(trip.getBus().getCapacity());
        assertThat(found).extracting(Trip::getDepartureTime).containsOnly(trip.getDepartureTime());
        assertThat(found).extracting(Trip::getArrivalTime).containsOnly(trip.getArrivalTime());
        assertThat(found).extracting(Trip::getPrice).containsOnly(trip.getPrice());
    }

    @Test
    void givenTrips_whenGetTrips_thenStatus200() {
        Trip trip1 = createTestTrip();
        Trip trip2 = createTestTrip();

        List<Trip> found = List.of(restTemplate.getForObject("/api/trip", Trip[].class));

        assertThat(found).extracting(Trip::getPrice).contains(trip1.getPrice(), trip2.getPrice());
        assertThat(found).extracting(Trip::getDeparture).extracting(City::getName)
                         .contains(trip1.getDeparture().getName(), trip2.getDeparture().getName());
        assertThat(found).extracting(Trip::getArrival).extracting(City::getName)
                         .contains(trip1.getArrival().getName(), trip2.getArrival().getName());
        assertThat(found).extracting(Trip::getBus).extracting(Bus::getCapacity)
                         .contains(trip1.getBus().getCapacity(), trip2.getBus().getCapacity());
        assertThat(found).extracting(Trip::getDepartureTime)
                         .contains(trip1.getDepartureTime(), trip2.getDepartureTime());
        assertThat(found).extracting(Trip::getArrivalTime).contains(trip1.getArrivalTime(), trip2.getArrivalTime());
        assertThat(found).extracting(Trip::getPrice).contains(trip1.getPrice(), trip2.getPrice());
    }

    @Test
    void whenGetTripById_thenStatus200() {
        Trip trip = createTestTrip();

        Trip found = restTemplate.getForObject("/api/trip/" + trip.getId(), Trip.class);

        assertThat(found).isNotNull();
        assertThat(found.getPrice()).isEqualTo(trip.getPrice());
        assertThat(found.getDeparture().getName()).isEqualTo(trip.getDeparture().getName());
        assertThat(found.getArrival().getName()).isEqualTo(trip.getArrival().getName());
        assertThat(found.getBus().getCapacity()).isEqualTo(trip.getBus().getCapacity());
        assertThat(found.getDepartureTime()).isEqualTo(trip.getDepartureTime());
        assertThat(found.getArrivalTime()).isEqualTo(trip.getArrivalTime());
        assertThat(found.getPrice()).isEqualTo(trip.getPrice());
    }

    @Test
    void whenGetTripByIdWithCurrencyUsd_thenStatus200() {
        Trip trip = createTestTrip();

        Trip found = restTemplate.getForObject("/api/trip/" + trip.getId() + "?currency=USD", Trip.class);

        assertThat(found).isNotNull();
        assertThat(found.getDeparture().getName()).isEqualTo(trip.getDeparture().getName());
        assertThat(found.getArrival().getName()).isEqualTo(trip.getArrival().getName());
        assertThat(found.getBus().getCapacity()).isEqualTo(trip.getBus().getCapacity());
        assertThat(found.getDepartureTime()).isEqualTo(trip.getDepartureTime());
        assertThat(found.getArrivalTime()).isEqualTo(trip.getArrivalTime());
        assertThat(found.getPrice()).isNotEqualTo(trip.getPrice());
    }

    @Test
    void whenGetTripByInvalidId_thenStatus404() {
        createTestTrip();

        Trip found = restTemplate.getForObject("/api/trip/999", Trip.class);

        assertThat(found).isNull();
    }

    @Test
    void whenUpdateTrip_thenStatus200() {
        Trip trip = createTestTrip();

        trip.setPrice(20.0);
        restTemplate.put("/api/trip/" + trip.getId(), trip);

        Trip updatedTrip = repository.findById(trip.getId()).orElse(null);
        assertThat(updatedTrip).isNotNull();
        assertThat(updatedTrip.getPrice()).isEqualTo(20.0);
    }

    @Test
    void whenDeleteTrip_thenStatus200() {
        Trip trip = createTestTrip();

        restTemplate.delete("/api/trip/" + trip.getId());

        assertThat(repository.findById(trip.getId())).isEmpty();
    }

    private Trip createTestTrip() {
        Bus bus = new Bus();
        bus.setCapacity(50);
        bus = busRepository.saveAndFlush(bus);

        City city = new City();
        city.setName("Aveiro");
        city = cityRepository.saveAndFlush(city);

        Trip trip = new Trip();
        trip.setDeparture(city);
        trip.setDepartureTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        trip.setArrival(city);
        trip.setArrivalTime(LocalDateTime.now().plusHours(3).truncatedTo(ChronoUnit.SECONDS));
        trip.setBus(bus);
        trip.setPrice(10.0);
        return repository.saveAndFlush(trip);
    }
}
