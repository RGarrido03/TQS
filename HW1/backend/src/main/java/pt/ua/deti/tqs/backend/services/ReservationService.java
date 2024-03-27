package pt.ua.deti.tqs.backend.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pt.ua.deti.tqs.backend.entities.Reservation;
import pt.ua.deti.tqs.backend.repositories.ReservationRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final TripService tripService;

    public Reservation createReservation(Reservation reservation) {
        Integer totalSeats = tripService.getFreeSeatsById(reservation.getTrip().getId());
        if (totalSeats < reservation.getSeats()) {
            return null;
        }
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getReservation(Long id) {
        return reservationRepository.findById(id).orElse(null);
    }

    public List<Reservation> getReservationsByUserId(Long userId) {
        return reservationRepository.findByUserId(userId);
    }

    public List<Reservation> getReservationsByTripId(Long tripId) {
        return reservationRepository.findByTripId(tripId);
    }

    public Reservation updateReservation(Reservation reservation) {
        Optional<Reservation> existingOpt = reservationRepository.findById(reservation.getId());

        if (existingOpt.isEmpty()) {
            return null;
        }

        Reservation existing = existingOpt.get();
        existing.setUser(reservation.getUser());
        existing.setSeats(reservation.getSeats());
        existing.setTrip(reservation.getTrip());
        existing.setPrice(reservation.getPrice());
        return reservationRepository.save(existing);
    }

    public void deleteReservationById(Long id) {
        reservationRepository.deleteById(id);
    }
}
