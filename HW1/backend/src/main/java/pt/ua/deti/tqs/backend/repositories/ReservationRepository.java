package pt.ua.deti.tqs.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.ua.deti.tqs.backend.entities.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
