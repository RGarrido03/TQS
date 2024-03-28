package pt.ua.deti.tqs.backend.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pt.ua.deti.tqs.backend.entities.Reservation;
import pt.ua.deti.tqs.backend.entities.Trip;
import pt.ua.deti.tqs.backend.entities.User;
import pt.ua.deti.tqs.backend.helpers.Currency;
import pt.ua.deti.tqs.backend.repositories.ReservationRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final TripService tripService;
    private final UserService userService;
    private final CurrencyService currencyService;

    public Reservation createReservation(Reservation reservation, Currency currency) {
        Trip trip = tripService.getTrip(reservation.getTrip().getId(), currency);

        if (trip.getFreeSeats() < reservation.getSeats()) {
            return null;
        }

        User user = userService.getUser(reservation.getUser().getId());
        reservation.setTrip(trip);
        reservation.setUser(user);

        if (currency != null && currency != Currency.EUR) {
            reservation.setPrice(currencyService.convertCurrencyToEur(reservation.getPrice(), currency));
        }

        return reservationRepository.save(reservation);
    }

    public List<Reservation> getAllReservations(Currency currency) {
        List<Reservation> all = reservationRepository.findAll();

        if (currency != null && currency != Currency.EUR) {
            all.forEach(reservation -> reservation.setPrice(
                    currencyService.convertEurToCurrency(reservation.getPrice(), currency)));
        }
        return all;
    }

    public Reservation getReservation(Long id, Currency currency) {
        Reservation reservation = reservationRepository.findById(id).orElse(null);

        if (reservation != null && currency != null && currency != Currency.EUR) {
            reservation.setPrice(currencyService.convertEurToCurrency(reservation.getPrice(), currency));
        }
        return reservation;
    }

    public List<Reservation> getReservationsByUserId(Long userId, Currency currency) {
        List<Reservation> all = reservationRepository.findByUserId(userId);

        if (currency != null && currency != Currency.EUR) {
            all.forEach(reservation -> reservation.setPrice(
                    currencyService.convertEurToCurrency(reservation.getPrice(), currency)));
        }
        return all;
    }

    public List<Reservation> getReservationsByTripId(Long tripId, Currency currency) {
        List<Reservation> all = reservationRepository.findByTripId(tripId);

        if (currency != null && currency != Currency.EUR) {
            all.forEach(reservation -> reservation.setPrice(
                    currencyService.convertEurToCurrency(reservation.getPrice(), currency)));
        }
        return all;
    }

    public Reservation updateReservation(Reservation reservation, Currency currency) {
        Optional<Reservation> existingOpt = reservationRepository.findById(reservation.getId());

        if (existingOpt.isEmpty()) {
            return null;
        }

        if (currency != null && currency != Currency.EUR) {
            reservation.setPrice(currencyService.convertCurrencyToEur(reservation.getPrice(), currency));
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
