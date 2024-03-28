package pt.ua.deti.tqs.backend.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pt.ua.deti.tqs.backend.entities.Bus;
import pt.ua.deti.tqs.backend.entities.City;
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
    private CityService cityService;
    private BusService busService;
    private TripRepository tripRepository;

    public Trip createTrip(Trip trip) {
        City departure = cityService.getCity(trip.getDeparture().getId());
        City arrival = cityService.getCity(trip.getArrival().getId());
        Bus bus = busService.getBus(trip.getBus().getId());

        trip.setDeparture(departure);
        trip.setArrival(arrival);
        trip.setBus(bus);
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
