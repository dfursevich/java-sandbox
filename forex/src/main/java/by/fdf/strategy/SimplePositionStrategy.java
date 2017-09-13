package by.fdf.strategy;

import by.fdf.domain.Indicators;
import by.fdf.domain.Position;
import by.fdf.domain.PriceBar;

import java.math.BigDecimal;

/**
 * @author Dzmitry Fursevich
 */
public class SimplePositionStrategy implements PositionStrategy {
    private BigDecimal stopLoss = new BigDecimal("0.001");
    private BigDecimal takeProfit = new BigDecimal("0.001");

    public SimplePositionStrategy(BigDecimal stopLoss, BigDecimal takeProfit) {
        this.stopLoss = stopLoss;
        this.takeProfit = takeProfit;
    }

    @Override
    public boolean open(PriceBar bar) {
        return true;
    }

    @Override
    public boolean close(Position position, PriceBar priceBar) {
        BigDecimal profit = position.profit(priceBar);

        return profit.signum() < 0 ? profit.abs().compareTo(stopLoss) >= 0 : profit.compareTo(takeProfit) >=0;
    }
}
