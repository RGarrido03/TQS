package pt.ua.deti.tqs.backend.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pt.ua.deti.tqs.backend.entities.Bus;
import pt.ua.deti.tqs.backend.entities.City;
import pt.ua.deti.tqs.backend.entities.Reservation;
import pt.ua.deti.tqs.backend.entities.Trip;
import pt.ua.deti.tqs.backend.services.ReservationService;
import pt.ua.deti.tqs.backend.services.TripService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TripController.class)
class TripControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private TripService service;

    @MockBean
    private ReservationService reservationService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    void whenPostTrip_thenCreateTrip() throws Exception {
        City city1 = new City();
        city1.setId(1L);
        city1.setName("Porto");

        City city2 = new City();
        city2.setId(2L);
        city2.setName("Lisboa");

        Bus bus = new Bus();
        bus.setId(1L);
        bus.setCapacity(50);

        Trip trip = new Trip();
        trip.setId(1L);
        trip.setDeparture(city1);
        trip.setArrival(city2);
        trip.setDepartureTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        trip.setArrivalTime(LocalDateTime.now().plusHours(3).truncatedTo(ChronoUnit.SECONDS));
        trip.setBus(bus);
        trip.setPrice(10.0);

        when(service.createTrip(Mockito.any())).thenReturn(trip);

        mvc.perform(post("/api/trip").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(trip)))
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.price", is(trip.getPrice())))
           .andExpect(jsonPath("$.bus.capacity", is(trip.getBus().getCapacity())))
           .andExpect(jsonPath("$.departure.name", is(trip.getDeparture().getName())))
           .andExpect(jsonPath("$.departureTime", is(trip.getDepartureTime().format(DateTimeFormatter.ISO_DATE_TIME))))
           .andExpect(jsonPath("$.arrival.name", is(trip.getArrival().getName())))
           .andExpect(jsonPath("$.arrivalTime", is(trip.getArrivalTime().format(DateTimeFormatter.ISO_DATE_TIME))));

        verify(service, times(1)).createTrip(Mockito.any());
    }

    @Test
    void givenManyTrips_whenGetTrips_thenReturnJsonArray() throws Exception {
        City city1 = new City();
        city1.setId(1L);
        city1.setName("Porto");

        City city2 = new City();
        city2.setId(2L);
        city2.setName("Lisboa");

        Bus bus = new Bus();
        bus.setId(1L);
        bus.setCapacity(50);

        Trip trip1 = new Trip();
        trip1.setId(1L);
        trip1.setDeparture(city1);
        trip1.setArrival(city2);
        trip1.setDepartureTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        trip1.setArrivalTime(LocalDateTime.now().plusHours(3).truncatedTo(ChronoUnit.SECONDS));
        trip1.setBus(bus);
        trip1.setPrice(10.0);

        Trip trip2 = new Trip();
        trip2.setId(2L);
        trip2.setDeparture(city2);
        trip2.setArrival(city1);
        trip2.setDepartureTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        trip2.setArrivalTime(LocalDateTime.now().plusHours(3).truncatedTo(ChronoUnit.SECONDS));
        trip2.setBus(bus);
        trip2.setPrice(10.0);

        when(service.getAllTrips()).thenReturn(List.of(trip1, trip2));

        mvc.perform(get("/api/trip").contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].price", is(trip1.getPrice())))
           .andExpect(jsonPath("$[0].bus.capacity", is(trip1.getBus().getCapacity())))
           .andExpect(jsonPath("$[0].departure.name", is(trip1.getDeparture().getName())))
           .andExpect(
                   jsonPath("$[0].departureTime", is(trip1.getDepartureTime().format(DateTimeFormatter.ISO_DATE_TIME))))
           .andExpect(jsonPath("$[0].arrival.name", is(trip1.getArrival().getName())))
           .andExpect(jsonPath("$[0].arrivalTime", is(trip1.getArrivalTime().format(DateTimeFormatter.ISO_DATE_TIME))))
           .andExpect(jsonPath("$[1].price", is(trip2.getPrice())))
           .andExpect(jsonPath("$[1].bus.capacity", is(trip2.getBus().getCapacity())))
           .andExpect(jsonPath("$[1].departure.name", is(trip2.getDeparture().getName())))
           .andExpect(
                   jsonPath("$[1].departureTime", is(trip2.getDepartureTime().format(DateTimeFormatter.ISO_DATE_TIME))))
           .andExpect(jsonPath("$[1].arrival.name", is(trip2.getArrival().getName())))
           .andExpect(jsonPath("$[1].arrivalTime", is(trip2.getArrivalTime().format(DateTimeFormatter.ISO_DATE_TIME))));
    }

    @Test
    void whenGetTripById_thenReturnTrip() throws Exception {
        City city1 = new City();
        city1.setId(1L);
        city1.setName("Porto");

        City city2 = new City();
        city2.setId(2L);
        city2.setName("Lisboa");

        Bus bus = new Bus();
        bus.setId(1L);
        bus.setCapacity(50);

        Trip trip = new Trip();
        trip.setId(1L);
        trip.setDeparture(city1);
        trip.setArrival(city2);
        trip.setDepartureTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        trip.setArrivalTime(LocalDateTime.now().plusHours(3).truncatedTo(ChronoUnit.SECONDS));
        trip.setBus(bus);
        trip.setPrice(10.0);

        when(service.getTrip(1L)).thenReturn(trip);

        mvc.perform(get("/api/trip/1").contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.price", is(trip.getPrice())))
           .andExpect(jsonPath("$.bus.capacity", is(trip.getBus().getCapacity())))
           .andExpect(jsonPath("$.departure.name", is(trip.getDeparture().getName())))
           .andExpect(jsonPath("$.departureTime", is(trip.getDepartureTime().format(DateTimeFormatter.ISO_DATE_TIME))))
           .andExpect(jsonPath("$.arrival.name", is(trip.getArrival().getName())))
           .andExpect(jsonPath("$.arrivalTime", is(trip.getArrivalTime().format(DateTimeFormatter.ISO_DATE_TIME))));

        verify(service, times(1)).getTrip(1L);
    }

    @Test
    void whenGetTripByInvalidId_thenReturnNotFound() throws Exception {
        when(service.getTrip(1L)).thenReturn(null);

        mvc.perform(get("/api/trip/1").contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isNotFound());

        verify(service, times(1)).getTrip(1L);
    }

    @Test
    void whenGetReservationsByTripId_thenReturnReservations() throws Exception {
        City city1 = new City();
        city1.setId(1L);
        city1.setName("Porto");

        City city2 = new City();
        city2.setId(2L);
        city2.setName("Lisboa");

        Bus bus = new Bus();
        bus.setId(1L);
        bus.setCapacity(50);

        Trip trip = new Trip();
        trip.setId(1L);
        trip.setDeparture(city1);
        trip.setArrival(city2);
        trip.setDepartureTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        trip.setArrivalTime(LocalDateTime.now().plusHours(3).truncatedTo(ChronoUnit.SECONDS));
        trip.setBus(bus);
        trip.setPrice(10.0);

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setTrip(trip);
        reservation.setSeats(1);

        when(reservationService.getReservationsByTripId(1L)).thenReturn(List.of(reservation));

        mvc.perform(get("/api/trip/1/reservations").contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].seats", is(1)))
           .andExpect(jsonPath("$[0].trip.id", is(1)));

        verify(reservationService, times(1)).getReservationsByTripId(1L);
    }

    @Test
    void whenGetTripsByArrivalId_thenReturnTrips() throws Exception {
        City city1 = new City();
        city1.setId(1L);
        city1.setName("Porto");

        City city2 = new City();
        city2.setId(2L);
        city2.setName("Lisboa");

        Bus bus = new Bus();
        bus.setId(1L);
        bus.setCapacity(50);

        Trip trip = new Trip();
        trip.setId(1L);
        trip.setDeparture(city1);
        trip.setArrival(city2);
        trip.setDepartureTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        trip.setArrivalTime(LocalDateTime.now().plusHours(3).truncatedTo(ChronoUnit.SECONDS));
        trip.setBus(bus);
        trip.setPrice(10.0);

        when(service.getTripsByArrivalId(2L)).thenReturn(List.of(trip));

        mvc.perform(get("/api/trip/arrivals/2").contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].price", is(trip.getPrice())))
           .andExpect(jsonPath("$[0].bus.capacity", is(trip.getBus().getCapacity())))
           .andExpect(jsonPath("$[0].departure.name", is(trip.getDeparture().getName())))
           .andExpect(
                   jsonPath("$[0].departureTime", is(trip.getDepartureTime().format(DateTimeFormatter.ISO_DATE_TIME))))
           .andExpect(jsonPath("$[0].arrival.name", is(trip.getArrival().getName())))
           .andExpect(jsonPath("$[0].arrivalTime", is(trip.getArrivalTime().format(DateTimeFormatter.ISO_DATE_TIME))));

        verify(service, times(1)).getTripsByArrivalId(2L);
    }

    @Test
    void whenGetTripsByArrivalIdAndDepartureTimeAfter_thenReturnTrips() throws Exception {
        City city1 = new City();
        city1.setId(1L);
        city1.setName("Porto");

        City city2 = new City();
        city2.setId(2L);
        city2.setName("Lisboa");

        Bus bus = new Bus();
        bus.setId(1L);
        bus.setCapacity(50);

        LocalDateTime departureTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime arrivalTime = LocalDateTime.now().plusHours(3).truncatedTo(ChronoUnit.SECONDS);

        Trip trip = new Trip();
        trip.setId(1L);
        trip.setDeparture(city1);
        trip.setArrival(city2);
        trip.setDepartureTime(departureTime);
        trip.setArrivalTime(arrivalTime);
        trip.setBus(bus);
        trip.setPrice(10.0);

        when(service.getTripsByArrivalIdAndDepartureTimeAfter(2L, departureTime)).thenReturn(List.of(trip));

        mvc.perform(get("/api/trip/arrivals/2?departureTime=" + departureTime.format(
                   DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].price", is(trip.getPrice())))
           .andExpect(jsonPath("$[0].bus.capacity", is(trip.getBus().getCapacity())))
           .andExpect(jsonPath("$[0].departure.name", is(trip.getDeparture().getName())))
           .andExpect(
                   jsonPath("$[0].departureTime", is(trip.getDepartureTime().format(DateTimeFormatter.ISO_DATE_TIME))))
           .andExpect(jsonPath("$[0].arrival.name", is(trip.getArrival().getName())))
           .andExpect(jsonPath("$[0].arrivalTime", is(trip.getArrivalTime().format(DateTimeFormatter.ISO_DATE_TIME))));

        verify(service, times(1)).getTripsByArrivalIdAndDepartureTimeAfter(2L, LocalDateTime.now().truncatedTo(
                ChronoUnit.SECONDS));
    }

    @Test
    void whenGetTripsByDepartureId_thenReturnTrips() throws Exception {
        City city1 = new City();
        city1.setId(1L);
        city1.setName("Porto");

        City city2 = new City();
        city2.setId(2L);
        city2.setName("Lisboa");

        Bus bus = new Bus();
        bus.setId(1L);
        bus.setCapacity(50);

        Trip trip = new Trip();
        trip.setId(1L);
        trip.setDeparture(city1);
        trip.setArrival(city2);
        trip.setDepartureTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        trip.setArrivalTime(LocalDateTime.now().plusHours(3).truncatedTo(ChronoUnit.SECONDS));
        trip.setBus(bus);
        trip.setPrice(10.0);

        when(service.getTripsByDepartureId(1L)).thenReturn(List.of(trip));

        mvc.perform(get("/api/trip/departures/1").contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].price", is(trip.getPrice())))
           .andExpect(jsonPath("$[0].bus.capacity", is(trip.getBus().getCapacity())))
           .andExpect(jsonPath("$[0].departure.name", is(trip.getDeparture().getName())))
           .andExpect(
                   jsonPath("$[0].departureTime", is(trip.getDepartureTime().format(DateTimeFormatter.ISO_DATE_TIME))))
           .andExpect(jsonPath("$[0].arrival.name", is(trip.getArrival().getName())))
           .andExpect(jsonPath("$[0].arrivalTime", is(trip.getArrivalTime().format(DateTimeFormatter.ISO_DATE_TIME))));

        verify(service, times(1)).getTripsByDepartureId(1L);
    }

    @Test
    void whenGetFreeSeatsById_thenReturnFreeSeats() throws Exception {
        City city1 = new City();
        city1.setId(1L);
        city1.setName("Porto");

        City city2 = new City();
        city2.setId(2L);
        city2.setName("Lisboa");

        Bus bus = new Bus();
        bus.setId(1L);
        bus.setCapacity(50);

        Trip trip = new Trip();
        trip.setId(1L);
        trip.setDeparture(city1);
        trip.setArrival(city2);
        trip.setDepartureTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        trip.setArrivalTime(LocalDateTime.now().plusHours(3).truncatedTo(ChronoUnit.SECONDS));
        trip.setBus(bus);
        trip.setPrice(10.0);

        when(service.getFreeSeatsById(1L)).thenReturn(50);

        mvc.perform(get("/api/trip/1/freeSeats").contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$", is(50)));

        verify(service, times(1)).getFreeSeatsById(1L);
    }

    @Test
    void whenUpdateTrip_thenUpdateTrip() throws Exception {
        City city1 = new City();
        city1.setId(1L);
        city1.setName("Porto");

        City city2 = new City();
        city2.setId(2L);
        city2.setName("Lisboa");

        Bus bus = new Bus();
        bus.setId(1L);
        bus.setCapacity(50);

        Trip trip = new Trip();
        trip.setId(1L);
        trip.setDeparture(city1);
        trip.setArrival(city2);
        trip.setDepartureTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        trip.setArrivalTime(LocalDateTime.now().plusHours(3).truncatedTo(ChronoUnit.SECONDS));
        trip.setBus(bus);
        trip.setPrice(10.0);

        when(service.updateTrip(Mockito.any())).thenReturn(trip);

        mvc.perform(put("/api/trip/1").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(trip)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.price", is(trip.getPrice())))
           .andExpect(jsonPath("$.bus.capacity", is(trip.getBus().getCapacity())))
           .andExpect(jsonPath("$.departure.name", is(trip.getDeparture().getName())))
           .andExpect(jsonPath("$.departureTime", is(trip.getDepartureTime().format(DateTimeFormatter.ISO_DATE_TIME))))
           .andExpect(jsonPath("$.arrival.name", is(trip.getArrival().getName())))
           .andExpect(jsonPath("$.arrivalTime", is(trip.getArrivalTime().format(DateTimeFormatter.ISO_DATE_TIME))));

        verify(service, times(1)).updateTrip(Mockito.any());
    }

    @Test
    void whenDeleteTrip_thenDeleteTrip() throws Exception {
        mvc.perform(delete("/api/trip/1").contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk());

        verify(service, times(1)).deleteTrip(1L);
    }
}
