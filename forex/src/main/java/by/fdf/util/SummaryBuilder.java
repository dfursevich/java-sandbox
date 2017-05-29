package by.fdf.util;

import by.fdf.domain.Position;
import by.fdf.domain.Summary;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Dzmitry Fursevich
 */
public class SummaryBuilder {

    private BigDecimal stopLoss;
    private BigDecimal takeProfit;
    private List<Position> positions;

    public SummaryBuilder setStopLoss(BigDecimal stopLoss) {
        this.stopLoss = stopLoss;
        return this;
    }

    public SummaryBuilder setTakeProfit(BigDecimal takeProfit) {
        this.takeProfit = takeProfit;
        return this;
    }

    public SummaryBuilder setPositions(List<Position> positions) {
        this.positions = positions;
        return this;
    }

    public Summary build() {
        BigDecimal profit = BigDecimal.ZERO;
        int profitCount = 0;
        int lossCount = 0;
        for (Position position : positions) {
            profit = profit.add(position.profit());
            if (position.profit().signum() > 0) {
                profitCount = profitCount + 1;
            } else if (position.profit().signum() < 0) {
                lossCount = lossCount + 1;
            }
        }

        return new Summary(stopLoss, takeProfit, profit, positions.size(), profitCount, lossCount);
    }
}
