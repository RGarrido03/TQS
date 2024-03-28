package pt.ua.deti.tqs.backend.services;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pt.ua.deti.tqs.backend.entities.Bus;
import pt.ua.deti.tqs.backend.entities.City;
import pt.ua.deti.tqs.backend.entities.Trip;
import pt.ua.deti.tqs.backend.helpers.Currency;
import pt.ua.deti.tqs.backend.repositories.TripRepository;
import pt.ua.deti.tqs.backend.specifications.trip.TripSearchParameters;
import pt.ua.deti.tqs.backend.specifications.trip.TripSearchParametersSpecification;

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

        trip.calculateFreeSeats();

        return tripRepository.save(trip);
    }

    public List<Trip> getTrips(TripSearchParameters params, Currency currency) {
        Specification<Trip> specification = new TripSearchParametersSpecification(params);
        List<Trip> all = tripRepository.findAll(specification);

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

        Trip save = tripRepository.save(existing);
        save.calculateFreeSeats();
        return save;
    }

    public void deleteTrip(Long id) {
        tripRepository.deleteById(id);
    }
}
