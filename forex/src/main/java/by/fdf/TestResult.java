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

    public void append(BigDecimal profit) {
        totalProfit = totalProfit.add(profit);
        totalCount = totalCount + 1;
        if (profit.compareTo(BigDecimal.ZERO) >= 0) {
            profitCount = profitCount + 1;
        } else {
            lossCount = lossCount + 1;
        }
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
