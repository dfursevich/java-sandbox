package by.fdf;

import java.math.BigDecimal;

/**
 * @author Dzmitry Fursevich
 */
public class TestResult {
    private BigDecimal totalProfit = BigDecimal.ZERO;
    private int totalCount = 0;
    private int profitCount = 0;
    private int lossCount = 0;

    public void append(Position position) {
        totalProfit = totalProfit.add(position.profit());
        totalCount = totalCount + 1;
        if (position.profit().signum() >= 0) {
            profitCount = profitCount + 1;
        } else {
            lossCount = lossCount + 1;
        }
    }

    public BigDecimal getTotalProfit() {
        return totalProfit;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getProfitCount() {
        return profitCount;
    }

    public int getLossCount() {
        return lossCount;
    }

    @Override
    public String toString() {
        return "TestResult{" +
                "\ntotalProfit=" + totalProfit +
                ", \ntotalCount=" + totalCount +
                ", \nprofitCount=" + profitCount +
                ", \nlossCount=" + lossCount +
                "\n}";
    }
}
