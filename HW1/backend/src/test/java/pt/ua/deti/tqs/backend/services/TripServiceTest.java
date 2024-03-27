package pt.ua.deti.tqs.backend.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.ua.deti.tqs.backend.entities.Bus;
import pt.ua.deti.tqs.backend.entities.City;
import pt.ua.deti.tqs.backend.entities.Reservation;
import pt.ua.deti.tqs.backend.entities.Trip;
import pt.ua.deti.tqs.backend.repositories.TripRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TripServiceTest {
    @Mock(lenient = true)
    private TripRepository tripRepository;

    @InjectMocks
    private TripService tripService;

    @BeforeEach
    public void setUp() {
        City city1 = new City();
        city1.setId(1L);
        city1.setName("Aveiro");

        City city2 = new City();
        city2.setId(2L);
        city2.setName("Porto");

        Bus bus = new Bus();
        bus.setId(1L);
        bus.setCapacity(50);

        Trip trip1 = new Trip();
        trip1.setId(1L);
        trip1.setDepartureTime(LocalDateTime.now());
        trip1.setArrivalTime(LocalDateTime.now().plusHours(1));
        trip1.setPrice(10.0);
        trip1.setDeparture(city1);
        trip1.setArrival(city2);
        trip1.setBus(bus);

        Trip trip2 = new Trip();
        trip2.setId(2L);
        trip2.setDepartureTime(LocalDateTime.now().minusHours(1));
        trip2.setArrivalTime(LocalDateTime.now().plusHours(1));
        trip2.setPrice(10.0);
        trip2.setDeparture(city2);
        trip2.setArrival(city1);
        trip2.setBus(bus);

        Trip trip3 = new Trip();
        trip3.setId(3L);
        trip3.setDepartureTime(LocalDateTime.now().plusHours(1));
        trip3.setArrivalTime(LocalDateTime.now().plusHours(2));
        trip3.setPrice(10.0);
        trip3.setDeparture(city1);
        trip3.setArrival(city2);
        trip3.setBus(bus);

        Reservation reservation = new Reservation();
        reservation.setSeats(1);
        reservation.setTrip(trip1);

        Mockito.when(tripRepository.findById(12345L)).thenReturn(Optional.empty());
        Mockito.when(tripRepository.findById(trip1.getId())).thenReturn(Optional.of(trip1));
        Mockito.when(tripRepository.findAll()).thenReturn(List.of(trip1, trip2, trip3));
        Mockito.when(tripRepository.findTripsByArrivalId(city1.getId())).thenReturn(List.of(trip1, trip3));
        Mockito.when(tripRepository.findTripsByArrivalId(city2.getId())).thenReturn(List.of(trip2));
        Mockito.when(tripRepository.findTripsByArrivalIdAndDepartureTimeAfter(Mockito.eq(city1.getId()), Mockito.any()))
               .thenReturn(List.of(trip3));
        Mockito.when(tripRepository.findTripsByDepartureId(city1.getId())).thenReturn(List.of(trip1, trip3));
        Mockito.when(tripRepository.save(trip1)).thenReturn(trip1);
    }

    @Test
    void whenSearchValidId_thenTripShouldBeFound() {
        Trip found = tripService.getTrip(1L);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(1L);
    }

    @Test
    void whenSearchInvalidId_thenTripShouldNotBeFound() {
        Trip fromDb = tripService.getTrip(12345L);
        assertThat(fromDb).isNull();
    }

    @Test
    void whenFindAllTrips_thenReturnAllTrips() {
        List<Trip> allTrips = tripService.getAllTrips();

        assertThat(allTrips).isNotNull().hasSize(3);
    }

    @Test
    void whenFindTripsByArrivalId_thenReturnTrips() {
        List<Trip> found = tripService.getTripsByArrivalId(1L);

        assertThat(found).hasSize(2);
    }

    @Test
    void whenFindTripsByArrivalIdAndDepartureTimeAfter_thenReturnTrips() {
        List<Trip> found = tripService.getTripsByArrivalIdAndDepartureTimeAfter(1L, LocalDateTime.now());

        assertThat(found).hasSize(1);
    }

    @Test
    void whenFindTripsByDepartureId_thenReturnTrips() {
        List<Trip> found = tripService.getTripsByDepartureId(1L);

        assertThat(found).hasSize(2);
    }

    @Test
    void whenGetFreeSeatsById_thenReturnFreeSeats() {
        Integer freeSeats = tripService.getFreeSeatsById(1L);

        assertThat(freeSeats).isNotNull().isEqualTo(50);
    }

    @Test
    void whenGetFreeSeatsByInvalidId_thenReturnNull() {
        Integer freeSeats = tripService.getFreeSeatsById(12345L);

        assertThat(freeSeats).isNull();
    }

    @Test
    void whenUpdateTrip_thenReturnUpdatedTrip() {
        Trip trip = new Trip();
        trip.setId(1L);
        trip.setDepartureTime(LocalDateTime.now().plusHours(1));
        trip.setArrivalTime(LocalDateTime.now().plusHours(2));
        trip.setPrice(20.0);

        Trip updated = tripService.updateTrip(trip);

        assertThat(updated).isNotNull();
        assertThat(updated.getId()).isEqualTo(1L);
        assertThat(updated.getDepartureTime()).isEqualTo(trip.getDepartureTime());
        assertThat(updated.getArrivalTime()).isEqualTo(trip.getArrivalTime());
        assertThat(updated.getPrice()).isEqualTo(trip.getPrice());
    }
}
