package by.fdf;

import java.math.BigDecimal;

/**
 * Created by Dmitry on 01.05.17.
 */
public class Position {
    private BigDecimal openPrice;
    private BigDecimal closePrice;
    private boolean sell;
    private boolean closed;

    public Position(BigDecimal openPrice, boolean sell) {
        this.openPrice = openPrice;
        this.sell = sell;
    }

    public BigDecimal profit(BigDecimal current) {
        return current.subtract(openPrice).multiply(sell ? new BigDecimal(-1) : new BigDecimal(1));
    }

    public boolean isClosed() {
        return closed;
    }

    public void close(BigDecimal closePrice) {
        this.closed = true;
        this.closePrice = closePrice;
    }

    public BigDecimal profit() {
        if (!closed) {
            throw new IllegalStateException("Position is not closed");
        }

        return closePrice.subtract(openPrice);
    }
}
