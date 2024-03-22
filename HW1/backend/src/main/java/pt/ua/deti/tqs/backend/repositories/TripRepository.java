package pt.ua.deti.tqs.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.ua.deti.tqs.backend.entities.Trip;

public interface TripRepository extends JpaRepository<Trip, Long> {
}
