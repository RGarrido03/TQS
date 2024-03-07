package pt.ua.deti.tqs.lab3_2;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarManagerService {
    private CarRepository carRepository;

    Car save(Car car) {
        return null;
    }

    List<Car> getAllCars() {
        return List.of();
    }

    Optional<Car> getCarDetails(Long carId) {
        return Optional.empty();
    }
}
