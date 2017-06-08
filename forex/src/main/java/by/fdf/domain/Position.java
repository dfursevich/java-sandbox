package by.fdf.domain;

import java.math.BigDecimal;

/**
 * Created by Dmitry on 01.05.17.
 */
public class Position {
    private PriceBar openPrice;
    private PriceBar closePrice;
    private PriceBar maxProfitPrice;
    private boolean sell;
    private boolean closed;

    public Position(PriceBar openPrice, boolean sell) {
        this.openPrice = openPrice;
        this.maxProfitPrice = openPrice;
        this.sell = sell;
    }

    public void update(PriceBar current) {
        if (profit(current).compareTo(profit(maxProfitPrice)) >= 0) {
            this.maxProfitPrice = current;
        }
    }

    public BigDecimal profit(PriceBar current) {
        return current.getClose().subtract(openPrice.getClose()).multiply(sell ? new BigDecimal(-1) : new BigDecimal(1));
    }

    public BigDecimal loss(PriceBar current) {
        return maxProfitPrice.getClose().subtract(current.getClose()).multiply(sell ? new BigDecimal(-1) : new BigDecimal(1));
    }

    public boolean isClosed() {
        return closed;
    }

    public void close(PriceBar closePrice) {
        if (closed) {
            throw new IllegalStateException("Position is closed");
        }

        this.closed = true;
        this.closePrice = closePrice;
    }

    public BigDecimal profit() {
        if (!closed) {
            throw new IllegalStateException("Position is not closed");
        }

        return closePrice.getClose().subtract(openPrice.getClose());
    }

    public PriceBar getOpenPrice() {
        return openPrice;
    }

    public PriceBar getClosePrice() {
        return closePrice;
    }
}
