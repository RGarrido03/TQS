package pt.ua.deti.tqs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StockTest {
    @InjectMocks
    StocksPortfolio stocksPortfolio;

    @Mock
    IStockmarketService stockmarketService;

    private Stock stock;

    @BeforeEach
    void setUp() {
        stock = new Stock("APPL", 10);
    }

    @Test
    public void testApp() {
        when(stockmarketService.lookUpPrice("APPL")).thenReturn(10.0);
        stocksPortfolio.addStock(stock);
        assertEquals(100.0, stocksPortfolio.totalValue());
    }
}
