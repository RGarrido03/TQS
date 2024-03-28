package pt.ua.deti.tqs.backend.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pt.ua.deti.tqs.backend.entities.Reservation;
import pt.ua.deti.tqs.backend.helpers.Currency;
import pt.ua.deti.tqs.backend.repositories.ReservationRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final TripService tripService;
    private final CurrencyService currencyService;

    public Reservation createReservation(Reservation reservation, Currency currency) {
        Integer totalSeats = tripService.getFreeSeatsById(reservation.getTrip().getId());
        if (totalSeats < reservation.getSeats()) {
            return null;
        }

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
