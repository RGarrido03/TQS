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

    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id).orElse(null);
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
