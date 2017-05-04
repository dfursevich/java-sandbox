package by.fdf;

import java.math.BigDecimal;

/**
 * Created by Dmitry on 01.05.17.
 */
public class Position {
    private BigDecimal price;
    private boolean sell;

    public Position(BigDecimal price, boolean sell) {
        this.price = price;
        this.sell = sell;
    }

    public BigDecimal profit(BigDecimal current) {
        return current.subtract(price).multiply(sell ? new BigDecimal(-1) : new BigDecimal(1));
    }
}
