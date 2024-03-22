package pt.ua.deti.tqs.backend.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pt.ua.deti.tqs.backend.entities.Reservation;
import pt.ua.deti.tqs.backend.entities.Trip;
import pt.ua.deti.tqs.backend.repositories.TripRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TripService {
    private TripRepository tripRepository;

    public Trip createTrip(Trip trip) {
        return tripRepository.save(trip);
    }

    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    public Trip getTrip(Long id) {
        return tripRepository.findById(id).orElse(null);
    }

    public List<Trip> getTripsByArrivalId(Long cityId) {
        return tripRepository.findTripsByArrivalId(cityId);
    }

    public List<Trip> getTripsByArrivalIdAndDepartureTimeAfter(Long cityId, LocalDateTime departureTime) {
        return tripRepository.findTripsByArrivalIdAAndDepartureTimeAfter(cityId, departureTime);
    }

    public List<Trip> getTripsByDepartureId(Long cityId) {
        return tripRepository.findTripsByDepartureId(cityId);
    }

    public Integer getFreeSeatsById(Long id) {
        Trip trip = tripRepository.findById(id).orElse(null);
        if (trip == null) {
            return null;
        }

        Integer totalSeats = trip.getBus().getCapacity();
        Integer reservedSeats = trip.getReservations().stream().mapToInt(Reservation::getSeats).sum();
        return totalSeats - reservedSeats;
    }

    public Trip updateTrip(Trip trip) {
        Optional<Trip> existingOpt = tripRepository.findById(trip.getId());

        if (existingOpt.isEmpty()) {
            return null;
        }

        Trip existing = existingOpt.get();
        existing.setDeparture(trip.getDeparture());
        existing.setArrival(trip.getArrival());
        existing.setDepartureTime(trip.getDepartureTime());
        existing.setArrivalTime(trip.getArrivalTime());
        existing.setPrice(trip.getPrice());
        return tripRepository.save(existing);
    }

    public void deleteTrip(Long id) {
        tripRepository.deleteById(id);
    }
}
