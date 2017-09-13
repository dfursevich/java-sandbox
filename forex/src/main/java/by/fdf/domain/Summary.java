package by.fdf.domain;

import java.math.BigDecimal;

/**
 * @author Dzmitry Fursevich
 */
public class Summary {
    private BigDecimal stopLoss;
    private BigDecimal takeProfit;
    private BigDecimal profit;
    private int duration;
    private int totalCount;
    private int profitCount;
    private int lossCount;

    public Summary() {
    }

    public Summary(BigDecimal stopLoss, BigDecimal takeProfit, BigDecimal profit, int duration, int totalCount, int profitCount, int lossCount) {
        this.stopLoss = stopLoss;
        this.takeProfit = takeProfit;
        this.profit = profit;
        this.duration = duration;
        this.totalCount = totalCount;
        this.profitCount = profitCount;
        this.lossCount = lossCount;
    }

    public BigDecimal getStopLoss() {
        return stopLoss;
    }

    public void setStopLoss(BigDecimal stopLoss) {
        this.stopLoss = stopLoss;
    }

    public BigDecimal getTakeProfit() {
        return takeProfit;
    }

    public void setTakeProfit(BigDecimal takeProfit) {
        this.takeProfit = takeProfit;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getProfitCount() {
        return profitCount;
    }

    public void setProfitCount(int profitCount) {
        this.profitCount = profitCount;
    }

    public int getLossCount() {
        return lossCount;
    }

    public void setLossCount(int lossCount) {
        this.lossCount = lossCount;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Summary{" +
                "stopLoss=" + stopLoss +
                ", takeProfit=" + takeProfit +
                ", profit=" + profit +
                ", totalCount=" + totalCount +
                ", profitCount=" + profitCount +
                ", lossCount=" + lossCount +
                '}';
    }
}
