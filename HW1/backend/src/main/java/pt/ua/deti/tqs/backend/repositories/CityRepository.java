package pt.ua.deti.tqs.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.ua.deti.tqs.backend.entities.City;

public interface CityRepository extends JpaRepository<City, Long> {
}
