package pt.ua.deti.tqs.backend.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pt.ua.deti.tqs.backend.entities.Reservation;
import pt.ua.deti.tqs.backend.entities.Trip;
import pt.ua.deti.tqs.backend.entities.User;
import pt.ua.deti.tqs.backend.services.ReservationService;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ReservationService service;

    @BeforeEach
    public void setUp() {
    }

    @Test
    void whenPostReservation_thenCreateReservation() throws Exception {
        User user = new User();
        user.setId(1L);

        Trip trip = new Trip();
        trip.setId(1L);

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setTrip(trip);
        reservation.setUser(user);
        reservation.setSeats(2);
        reservation.setPrice(10.0);

        when(service.createReservation(Mockito.any())).thenReturn(reservation);

        mvc.perform(
                   post("/api/reservation").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(reservation)))
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.seats", is(2)))
           .andExpect(jsonPath("$.price", is(10.0)))
           .andExpect(jsonPath("$.user.id", is(1)))
           .andExpect(jsonPath("$.trip.id", is(1)));

        verify(service, times(1)).createReservation(Mockito.any());
    }

    @Test
    void givenManyReservations_whenGetReservations_thenReturnJsonArray() throws Exception {
        User user = new User();
        user.setId(1L);

        Trip trip = new Trip();
        trip.setId(1L);

        Reservation reservation1 = new Reservation();
        reservation1.setId(1L);
        reservation1.setTrip(trip);
        reservation1.setUser(user);
        reservation1.setSeats(2);
        reservation1.setPrice(10.0);

        Reservation reservation2 = new Reservation();
        reservation2.setId(2L);
        reservation2.setTrip(trip);
        reservation2.setUser(user);
        reservation2.setSeats(3);
        reservation2.setPrice(15.0);

        Reservation reservation3 = new Reservation();
        reservation3.setId(3L);
        reservation3.setTrip(trip);
        reservation3.setUser(user);
        reservation3.setSeats(4);
        reservation3.setPrice(20.0);

        when(service.getAllReservations()).thenReturn(Arrays.asList(reservation1, reservation2, reservation3));

        mvc.perform(get("/api/reservation"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$", hasSize(3)))
           .andExpect(jsonPath("$[0].seats", is(2)))
           .andExpect(jsonPath("$[0].price", is(10.0)))
           .andExpect(jsonPath("$[0].user.id", is(1)))
           .andExpect(jsonPath("$[0].trip.id", is(1)))
           .andExpect(jsonPath("$[1].seats", is(3)))
           .andExpect(jsonPath("$[1].price", is(15.0)))
           .andExpect(jsonPath("$[1].user.id", is(1)))
           .andExpect(jsonPath("$[1].trip.id", is(1)))
           .andExpect(jsonPath("$[2].seats", is(4)))
           .andExpect(jsonPath("$[2].price", is(20.0)))
           .andExpect(jsonPath("$[2].user.id", is(1)))
           .andExpect(jsonPath("$[2].trip.id", is(1)));

        verify(service, times(1)).getAllReservations();
    }

    @Test
    void whenGetReservationById_thenReturnReservation() throws Exception {
        User user = new User();
        user.setId(1L);

        Trip trip = new Trip();
        trip.setId(1L);

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setTrip(trip);
        reservation.setUser(user);
        reservation.setSeats(2);
        reservation.setPrice(10.0);

        when(service.getReservation(1L)).thenReturn(reservation);

        mvc.perform(get("/api/reservation/1"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.seats", is(2)))
           .andExpect(jsonPath("$.price", is(10.0)))
           .andExpect(jsonPath("$.user.id", is(1)))
           .andExpect(jsonPath("$.trip.id", is(1)));

        verify(service, times(1)).getReservation(1L);
    }

    @Test
    void whenGetReservationByInvalidId_thenReturnNotFound() throws Exception {
        when(service.getReservation(1L)).thenReturn(null);

        mvc.perform(get("/api/reservation/1"))
           .andExpect(status().isNotFound());

        verify(service, times(1)).getReservation(1L);
    }

    @Test
    void whenUpdateReservation_thenUpdateReservation() throws Exception {
        User user = new User();
        user.setId(1L);

        Trip trip = new Trip();
        trip.setId(1L);

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setTrip(trip);
        reservation.setUser(user);
        reservation.setSeats(2);
        reservation.setPrice(10.0);

        when(service.updateReservation(Mockito.any())).thenReturn(reservation);

        mvc.perform(put("/api/reservation/1").contentType(MediaType.APPLICATION_JSON)
                                             .content(JsonUtils.toJson(reservation)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.seats", is(2)))
           .andExpect(jsonPath("$.price", is(10.0)))
           .andExpect(jsonPath("$.user.id", is(1)))
           .andExpect(jsonPath("$.trip.id", is(1)));

        verify(service, times(1)).updateReservation(Mockito.any());
    }

    @Test
    void whenDeleteReservation_thenDeleteReservation() throws Exception {
        mvc.perform(delete("/api/reservation/1"))
           .andExpect(status().isOk());

        verify(service, times(1)).deleteReservationById(1L);
    }
}
