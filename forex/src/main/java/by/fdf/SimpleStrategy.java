package by.fdf;

import java.math.BigDecimal;

/**
 * Created by Dmitry on 01.05.17.
 */
public class SimpleStrategy implements Strategy {
    private int positionNumber = 0;
    private int profitPositionNumber = 0;

    private Position position;
    private BigDecimal high;
    private BigDecimal low;

    private BigDecimal total = new BigDecimal(0);
    private BigDecimal stopLoss = new BigDecimal("0.001");
    private BigDecimal takeProfit = new BigDecimal("0.002");

    public SimpleStrategy(BigDecimal stopLoss, BigDecimal takeProfit) {
        this.stopLoss = stopLoss;
        this.takeProfit = takeProfit;
    }

    @Override
    public void tick(BigDecimal current) {
        if (position == null) {
            position = new Position(current, false);
        }

        high = high != null ? high.max(current) : current;
        low = low != null ? low.min(current) : current;

        BigDecimal loss = high.subtract(current);
        BigDecimal profit = position.profit(current);

        if (profit.multiply(BigDecimal.valueOf(-1)).compareTo(stopLoss) >= 0) {
            closePosition(profit);
        } else if (profit.compareTo(takeProfit) >= 0) {
            closePosition(profit);
        }
    }

    private void closePosition(BigDecimal profit) {
        total = total.add(profit);
        position = null;
        positionNumber++;
        if (profit.compareTo(BigDecimal.ZERO) >= 0) {
            profitPositionNumber++;
        }

        high = null;
        low = null;
    }


    public BigDecimal getTotal() {
        return total;
    }

    @Override
    public void print() {
        System.out.println("total: " + total);
        System.out.println("positionNumber = " + positionNumber);
        System.out.println("profitPositionNumber = " + profitPositionNumber);
    }
}
