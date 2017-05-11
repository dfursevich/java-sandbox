package by.fdf;

import java.math.BigDecimal;

/**
 * @author Dzmitry Fursevich
 */
public class PositionStrategyImpl implements PositionStrategy {
    private BigDecimal stopLoss = new BigDecimal("0.001");
    private BigDecimal takeProfit = new BigDecimal("0.001");

    public PositionStrategyImpl(BigDecimal stopLoss, BigDecimal takeProfit) {
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
