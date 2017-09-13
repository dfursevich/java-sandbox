package by.fdf.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Dzmitry Fursevich
 */
public class Indicators {
    private Date date;
    private BigDecimal shortEMA;
    private BigDecimal longEMA;

    public Indicators() {
    }

    public Indicators(Date date, BigDecimal shortEMA, BigDecimal longEMA) {
        this.date = date;
        this.shortEMA = shortEMA;
        this.longEMA = longEMA;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getShortEMA() {
        return shortEMA;
    }

    public void setShortEMA(BigDecimal shortEMA) {
        this.shortEMA = shortEMA;
    }

    public BigDecimal getLongEMA() {
        return longEMA;
    }

    public void setLongEMA(BigDecimal longEMA) {
        this.longEMA = longEMA;
    }
}
