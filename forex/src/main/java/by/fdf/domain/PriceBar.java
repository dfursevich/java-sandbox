package by.fdf.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Dzmitry Fursevich
 */
public class PriceBar {
    private Date date;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;

    public PriceBar(Date date, BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close) {
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
    }

    public Date getDate() {
        return date;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public BigDecimal getClose() {
        return close;
    }
}
