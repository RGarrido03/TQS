package pt.ua.deti.tqs.backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ua.deti.tqs.backend.entities.Reservation;
import pt.ua.deti.tqs.backend.services.ReservationService;

import java.util.List;

@RestController
@RequestMapping("api/reservation")
@Tag(name = "Reservation")
@AllArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    @Operation(summary = "Create a new reservation")
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        Reservation created = reservationService.createReservation(reservation);
        HttpStatus status = created != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(created, status);
    }

    @GetMapping
    @Operation(summary = "Get all reservations")
    public ResponseEntity<List<Reservation>> getReservations() {
        return new ResponseEntity<>(reservationService.getAllReservations(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    @Operation(summary = "Get a reservation")
    public ResponseEntity<Reservation> getReservation(
            @PathVariable @Parameter(name = "Reservation ID", example = "1") Long id) {
        Reservation reservation = reservationService.getReservation(id);
        HttpStatus status = reservation != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(reservation, status);
    }

    @PutMapping("{id}")
    @Operation(summary = "Update a reservation")
    public ResponseEntity<Reservation> updateReservation(
            @PathVariable @Parameter(name = "Reservation ID", example = "1") Long id, @RequestBody Reservation reservation) {
        reservation.setId(id);
        Reservation updated = reservationService.updateReservation(reservation);
        HttpStatus status = updated != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(updated, status);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete a reservation")
    public ResponseEntity<Void> deleteReservation(
            @PathVariable @Parameter(name = "Reservation ID", example = "1") Long id) {
        reservationService.deleteReservationById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
