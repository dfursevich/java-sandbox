package by.fdf.util;

import by.fdf.domain.PriceBar;
import by.fdf.domain.Summary;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Dzmitry Fursevich
 */
public class Database {
    public static void populatePrices(JdbcTemplate jdbcTemplate, List<PriceBar> batch) {
        jdbcTemplate.update("DELETE FROM eurusd");

        jdbcTemplate.batchUpdate("INSERT INTO eurusd(time, open, high, low, close) VALUES(?, ?, ?, ?, ?)", batch, 1000, (preparedStatement, priceBar) -> {
            preparedStatement.setTimestamp(1, new Timestamp(priceBar.getDate().getTime()));
            preparedStatement.setBigDecimal(2, priceBar.getOpen());
            preparedStatement.setBigDecimal(3, priceBar.getHigh());
            preparedStatement.setBigDecimal(4, priceBar.getLow());
            preparedStatement.setBigDecimal(5, priceBar.getClose());
        });
    }

    public static void pupulateSummaries(JdbcTemplate jdbcTemplate, List<Summary> batch) {
        jdbcTemplate.update("DELETE FROM summary");

        jdbcTemplate.batchUpdate("INSERT INTO summary(stop_loss, take_profit, profit, total_count, profit_count, loss_count) VALUES(?, ?, ?, ?, ?, ?)", batch, 1000, (preparedStatement, summary) -> {
            preparedStatement.setBigDecimal(1, summary.getStopLoss());
            preparedStatement.setBigDecimal(2, summary.getTakeProfit());
            preparedStatement.setBigDecimal(3, summary.getProfit());
            preparedStatement.setInt(4, summary.getTotalCount());
            preparedStatement.setInt(5, summary.getProfitCount());
            preparedStatement.setInt(6, summary.getLossCount());
        });
    }
}
