package by.fdf.strategy;

import by.fdf.domain.Indicators;
import by.fdf.domain.Position;
import by.fdf.domain.PriceBar;

/**
 * Created by dfursevich on 21.05.17.
 */
public class AutoClosePositionStrategy implements PositionStrategy {
    @Override
    public boolean open(PriceBar bar) {
        return true;
    }

    @Override
    public boolean close(Position position, PriceBar priceBar) {
        return true;
    }
}
