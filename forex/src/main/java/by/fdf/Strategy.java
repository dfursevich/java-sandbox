package by.fdf;

import java.math.BigDecimal;

/**
 * Created by Dmitry on 01.05.17.
 */
public interface Strategy {
    void tick(BigDecimal price);

    BigDecimal getTotal();

    void print();
}
