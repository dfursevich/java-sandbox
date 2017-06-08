package by.fdf.strategy;

import by.fdf.domain.Position;
import by.fdf.domain.PriceBar;

import java.math.BigDecimal;

/**
 * @author Dzmitry Fursevich
 */
public class DefaultPositionStrategy implements PositionStrategy {
    private BigDecimal stopLoss;
    private BigDecimal takeProfit;

    public DefaultPositionStrategy(BigDecimal stopLoss, BigDecimal takeProfit) {
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
        BigDecimal loss = position.loss(priceBar);

        return profit.compareTo(takeProfit) >=0 || loss.compareTo(stopLoss) >= 0;
    }
}
