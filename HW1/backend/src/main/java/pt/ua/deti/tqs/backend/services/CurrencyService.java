package pt.ua.deti.tqs.backend.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pt.ua.deti.tqs.backend.helpers.Currency;
import pt.ua.deti.tqs.backend.helpers.CurrencyApiResponse;

import java.util.Map;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Slf4j
public class CurrencyService {
    public final String apiKey;
    private final RestTemplate restTemplate;
    private final CurrencyService self;

    public CurrencyService(@Value("${currencyapi.apikey}") String apiKey, RestTemplate restTemplate, CurrencyService self) {
        this.apiKey = apiKey;
        this.restTemplate = restTemplate;
        this.self = self;
    }

    @Cacheable("rates")
    public Map<Currency, Double> fetchRates() {
        log.info("Fetching rates");
        CurrencyApiResponse forObject = restTemplate.getForEntity(
                "https://api.freecurrencyapi.com/v1/latest?apikey=" + apiKey + "&base_currency=" + Currency.EUR,
                CurrencyApiResponse.class).getBody();
        return forObject != null ? forObject.getData() : null;
    }

    @CacheEvict(value = "rates", allEntries = true)
    @Scheduled(fixedRate = 3600000)
    public void refreshRatesCache() {
        log.info("Emptying rates cache");
        self.fetchRates();
    }

    public double convertEurToCurrency(double euros, Currency currency) {
        if (currency == Currency.EUR) {
            return euros;
        }

        Map<Currency, Double> response = self.fetchRates();
        return euros * response.get(currency);
    }

    public double convertCurrencyToEur(double amount, Currency currency) {
        if (currency == Currency.EUR) {
            return amount;
        }

        Map<Currency, Double> response = self.fetchRates();
        return amount / response.get(currency);
    }
}
