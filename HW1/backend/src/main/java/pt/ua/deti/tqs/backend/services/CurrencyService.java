package pt.ua.deti.tqs.backend.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pt.ua.deti.tqs.backend.helpers.Currency;

import java.util.Map;

@Service
@AllArgsConstructor
public class CurrencyService {
    private final ApiService apiService;

    public double convertEurToCurrency(double euros, Currency currency) {
        if (currency == Currency.EUR) {
            return euros;
        }

        Map<Currency, Double> response = apiService.fetchRates();
        return euros * response.get(currency);
    }

    public double convertCurrencyToEur(double amount, Currency currency) {
        if (currency == Currency.EUR) {
            return amount;
        }

        Map<Currency, Double> response = apiService.fetchRates();
        return amount / response.get(currency);
    }
}
