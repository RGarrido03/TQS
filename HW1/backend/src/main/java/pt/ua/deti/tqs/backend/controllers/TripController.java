package pt.ua.deti.tqs.backend.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ua.deti.tqs.backend.entities.Reservation;
import pt.ua.deti.tqs.backend.entities.Trip;
import pt.ua.deti.tqs.backend.services.ReservationService;
import pt.ua.deti.tqs.backend.services.TripService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/trip")
@AllArgsConstructor
public class TripController {
    private final TripService tripService;
    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<Trip> createTrip(@RequestBody Trip trip) {
        return new ResponseEntity<>(tripService.createTrip(trip), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Trip>> getTrips() {
        return new ResponseEntity<>(tripService.getAllTrips(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Trip> getTrip(@PathVariable Long id) {
        Trip trip = tripService.getTrip(id);
        HttpStatus status = trip != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(trip, status);
    }

    @GetMapping("{id}/reservations")
    public ResponseEntity<List<Reservation>> getReservationsByTripId(@PathVariable Long id) {
        List<Reservation> reservations = reservationService.getReservationsByTripId(id);
        HttpStatus status = !reservations.isEmpty() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(reservations, status);
    }

    @GetMapping("arrival?cityId={cityId}")
    public ResponseEntity<List<Trip>> getTripsByArrivalId(@PathVariable Long cityId) {
        return new ResponseEntity<>(tripService.getTripsByArrivalId(cityId), HttpStatus.OK);
    }

    @GetMapping("arrival?cityId={cityId}&departureTimeAfter={departureTime}")
    public ResponseEntity<List<Trip>> getTripsByArrivalIdAndDepartureTimeAfter(@PathVariable Long cityId, @PathVariable LocalDateTime departureTime) {
        return new ResponseEntity<>(tripService.getTripsByArrivalIdAndDepartureTimeAfter(cityId, departureTime),
                                    HttpStatus.OK);
    }

    @GetMapping("departure?cityId={cityId}")
    public ResponseEntity<List<Trip>> getTripsByDepartureId(@PathVariable Long cityId) {
        return new ResponseEntity<>(tripService.getTripsByDepartureId(cityId), HttpStatus.OK);
    }

    @GetMapping("{id}/freeSeats")
    public ResponseEntity<Integer> getFreeSeatsById(@PathVariable Long id) {
        Integer freeSeats = tripService.getFreeSeatsById(id);
        HttpStatus status = freeSeats != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(freeSeats, status);
    }

    @PutMapping("{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable Long id, @RequestBody Trip trip) {
        trip.setId(id);
        Trip updated = tripService.updateTrip(trip);
        HttpStatus status = updated != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(updated, status);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Long id) {
        tripService.deleteTrip(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
