package pt.ua.deti.tqs.backend.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.ua.deti.tqs.backend.entities.*;
import pt.ua.deti.tqs.backend.repositories.ReservationRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {
    @Mock(lenient = true)
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    public void setUp() {
        City city = new City();
        city.setId(2L);
        city.setName("Porto");

        Bus bus = new Bus();
        bus.setId(1L);
        bus.setCapacity(50);

        Trip trip = new Trip();
        trip.setId(1L);
        trip.setDepartureTime(LocalDateTime.now());
        trip.setArrivalTime(LocalDateTime.now().plusHours(1));
        trip.setPrice(10.0);
        trip.setDeparture(city);
        trip.setArrival(city);
        trip.setBus(bus);

        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setPassword("password");
        user.setEmail("user@ua.pt");
        user.setName("User");

        Reservation reservation1 = new Reservation();
        reservation1.setId(1L);
        reservation1.setSeats(1);
        reservation1.setTrip(trip);
        reservation1.setUser(user);
        reservation1.setPrice(10.0);

        Reservation reservation2 = new Reservation();
        reservation2.setId(2L);
        reservation2.setSeats(2);
        reservation2.setTrip(trip);
        reservation2.setUser(null);
        reservation2.setPrice(20.0);

        Reservation reservation3 = new Reservation();
        reservation3.setId(3L);
        reservation3.setSeats(3);
        reservation3.setTrip(null);
        reservation3.setUser(user);
        reservation3.setPrice(30.0);

        Mockito.when(reservationRepository.findById(reservation1.getId())).thenReturn(Optional.of(reservation1));
        Mockito.when(reservationRepository.findById(12345L)).thenReturn(Optional.empty());
        Mockito.when(reservationRepository.findAll()).thenReturn(List.of(reservation1, reservation2, reservation3));
        Mockito.when(reservationRepository.findByUserId(user.getId()))
               .thenReturn(List.of(reservation1, reservation3));
        Mockito.when(reservationRepository.findByTripId(trip.getId()))
               .thenReturn(List.of(reservation1, reservation2));
    }

    @Test
    void whenSearchValidId_thenReservationShouldBeFound() {
        Reservation found = reservationService.getReservation(1L, null);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(1L);
    }

    @Test
    void whenSearchInvalidId_thenReservationShouldNotBeFound() {
        Reservation found = reservationService.getReservation(12345L, null);

        assertThat(found).isNull();
    }

    @Test
    void whenFindAll_thenAllReservationsShouldBeFound() {
        List<Reservation> reservations = reservationService.getAllReservations(null);

        assertThat(reservations).isNotNull().hasSize(3);
    }

    @Test
    void whenFindByUserId_thenReservationsShouldBeFound() {
        List<Reservation> reservations = reservationService.getReservationsByUserId(1L, null);

        assertThat(reservations).isNotNull().hasSize(2);
    }

    @Test
    void whenFindByTripId_thenNoReservationsShouldBeFound() {
        List<Reservation> reservations = reservationService.getReservationsByTripId(1L, null);

        assertThat(reservations).isNotNull().hasSize(2);
    }
}
