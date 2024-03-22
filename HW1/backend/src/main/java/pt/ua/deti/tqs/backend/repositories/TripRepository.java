package pt.ua.deti.tqs.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.ua.deti.tqs.backend.entities.Trip;

import java.time.LocalDateTime;
import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findTripsByArrivalId(Long cityId);

    List<Trip> findTripsByArrivalIdAndDepartureTimeAfter(Long cityId, LocalDateTime departureTime);

    List<Trip> findTripsByDepartureId(Long cityId);
}
