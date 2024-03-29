package pt.ua.deti.tqs.backend.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pt.ua.deti.tqs.backend.helpers.Currency;
import pt.ua.deti.tqs.backend.helpers.CurrencyApiResponse;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ApiServiceTest {
    @MockBean
    private RestTemplate restTemplate;

    @Mock(lenient = true)
    private CurrencyApiResponse currencyApiResponse;

    @Autowired
    @InjectMocks
    private ApiService apiService;

    @BeforeEach
    public void setUp() {
        Mockito.when(currencyApiResponse.getData())
               .thenReturn(Map.of(Currency.USD, 1.08, Currency.EUR, 1.0, Currency.GBP, 0.92));

        Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.eq(CurrencyApiResponse.class)))
               .thenReturn(new ResponseEntity<>(currencyApiResponse, null, 200));
    }

    @AfterEach
    public void tearDown() {
        apiService.emptyRatesCache();
    }

    @Test
    void whenFetchRates_thenRatesAreFetched() {
        apiService.fetchRates();
        Mockito.verify(restTemplate, Mockito.times(1))
               .getForEntity(Mockito.anyString(), Mockito.eq(CurrencyApiResponse.class));
    }

    @Test
    void whenFetchRatesTwice_thenCacheIsUsed() {
        apiService.fetchRates();
        apiService.fetchRates();
        Mockito.verify(restTemplate, Mockito.times(1))
               .getForEntity(Mockito.anyString(), Mockito.eq(CurrencyApiResponse.class));
    }
}
