package pt.ua.deti.tqs.backend.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pt.ua.deti.tqs.backend.entities.Reservation;
import pt.ua.deti.tqs.backend.entities.Trip;
import pt.ua.deti.tqs.backend.helpers.Currency;
import pt.ua.deti.tqs.backend.repositories.TripRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TripService {
    private CurrencyService currencyService;
    private TripRepository tripRepository;

    public Trip createTrip(Trip trip) {
        return tripRepository.save(trip);
    }

    public List<Trip> getAllTrips(Currency currency) {
        List<Trip> all = tripRepository.findAll();

        if (currency != null && currency != Currency.EUR) {
            all.forEach(trip -> trip.setPrice(currencyService.convertEurToCurrency(trip.getPrice(), currency)));
        }
        return all;
    }

    public Trip getTrip(Long id, Currency currency) {
        Trip trip = tripRepository.findById(id).orElse(null);

        if (trip != null && currency != null && currency != Currency.EUR) {
            trip.setPrice(currencyService.convertEurToCurrency(trip.getPrice(), currency));
        }
        return trip;
    }

    public List<Trip> getTripsByArrivalId(Long cityId, Currency currency) {
        List<Trip> all = tripRepository.findTripsByArrivalId(cityId);

        if (currency != null && currency != Currency.EUR) {
            all.forEach(trip -> trip.setPrice(currencyService.convertEurToCurrency(trip.getPrice(), currency)));
        }
        return all;
    }

    public List<Trip> getTripsByArrivalIdAndDepartureTimeAfter(Long cityId, LocalDateTime departureTime, Currency currency) {
        List<Trip> all = tripRepository.findTripsByArrivalIdAndDepartureTimeAfter(cityId, departureTime);

        if (currency != null && currency != Currency.EUR) {
            all.forEach(trip -> trip.setPrice(currencyService.convertEurToCurrency(trip.getPrice(), currency)));
        }
        return all;
    }

    public List<Trip> getTripsByDepartureId(Long cityId, Currency currency) {
        List<Trip> all = tripRepository.findTripsByDepartureId(cityId);

        if (currency != null && currency != Currency.EUR) {
            all.forEach(trip -> trip.setPrice(currencyService.convertEurToCurrency(trip.getPrice(), currency)));
        }
        return all;
    }

    public Integer getFreeSeatsById(Long id) {
        Trip trip = tripRepository.findById(id).orElse(null);
        if (trip == null) {
            return null;
        }

        Integer totalSeats = trip.getBus().getCapacity();
        Integer reservedSeats = trip.getReservations() != null
                ? trip.getReservations().stream().mapToInt(Reservation::getSeats).sum()
                : 0;
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
