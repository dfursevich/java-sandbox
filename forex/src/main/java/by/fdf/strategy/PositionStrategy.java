package by.fdf.strategy;

import by.fdf.domain.Position;
import by.fdf.domain.PriceBar;

/**
 * @author Dzmitry Fursevich
 */
public interface PositionStrategy {
    boolean open(PriceBar bar);

    boolean close(Position position, PriceBar priceBar);
}
