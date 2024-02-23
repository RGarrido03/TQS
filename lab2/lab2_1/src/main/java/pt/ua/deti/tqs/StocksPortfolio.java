package pt.ua.deti.tqs;

import java.util.ArrayList;
import java.util.List;

public class StocksPortfolio {
    private final IStockmarketService stockmarket;
    private final List<Stock> stocks;

    public StocksPortfolio(IStockmarketService stockmarket) {
        this.stockmarket = stockmarket;
        this.stocks = new ArrayList<>();
    }

    public void addStock(Stock stock) {
        stocks.add(stock);
    }

    public double totalValue() {
        return stocks.stream().map(stock -> stock.getQuantity() * stockmarket.lookUpPrice(stock.getLabel()))
                     .reduce(0.0, Double::sum);
    }
}
