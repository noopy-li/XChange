package org.knowm.xchange.examples.quant;

import java.io.IOException;
import java.math.BigDecimal;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.binance.v3.BinanceExchange;
import org.knowm.xchange.bitstamp.BitstampExchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.service.marketdata.MarketDataService;

/**
 * A very small example showing how XChange can be used as the core of a quantitative
 * trading system. It retrieves BTC prices from multiple exchanges and prints the
 * current spread between them. Real trading systems would extend this skeleton with
 * order execution, persistence and risk management.
 */
public class BasicArbitrageBot {

  public static void main(String[] args) throws IOException, InterruptedException {
    Exchange binance = ExchangeFactory.INSTANCE.createExchange(BinanceExchange.class);
    Exchange bitstamp = ExchangeFactory.INSTANCE.createExchange(BitstampExchange.class);

    MarketDataService binanceData = binance.getMarketDataService();
    MarketDataService bitstampData = bitstamp.getMarketDataService();

    for (int i = 0; i < 5; i++) {
      Ticker binanceTicker = binanceData.getTicker(CurrencyPair.BTC_USDT);
      Ticker bitstampTicker = bitstampData.getTicker(CurrencyPair.BTC_USD);

      BigDecimal spread = binanceTicker.getLast().subtract(bitstampTicker.getLast());
      System.out.printf(
          "Binance BTC/USDT: %s | Bitstamp BTC/USD: %s | Spread (USDT-USD): %s%n",
          binanceTicker.getLast(), bitstampTicker.getLast(), spread);

      Thread.sleep(5000L);
    }
  }
}
