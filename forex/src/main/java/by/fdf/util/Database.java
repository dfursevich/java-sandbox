package by.fdf.util;

import by.fdf.domain.Indicators;
import by.fdf.domain.Position;
import by.fdf.domain.PriceBar;
import by.fdf.domain.Summary;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

    public static int countPrices(JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.queryForObject("select count(*) from eurusd", Integer.class);
    }

    public static void populateSummaries(JdbcTemplate jdbcTemplate, List<Summary> batch) {
        jdbcTemplate.update("DELETE FROM summary");

        jdbcTemplate.batchUpdate("INSERT INTO summary(stop_loss, take_profit, profit, total_count, profit_count, loss_count) VALUES(?, ?, ?, ?, ?, ?)", batch, 1000, (preparedStatement, summary) -> {
            preparedStatement.setBigDecimal(1, summary.getStopLoss());
            preparedStatement.setBigDecimal(2, summary.getTakeProfit());
            preparedStatement.setBigDecimal(3, summary.getProfit());
            preparedStatement.setInt(4, summary.getTotalCount());
            preparedStatement.setInt(5, summary.getProfitCount());
            preparedStatement.setInt(6, summary.getLossCount());
        });

//        List<String> archivedTables = jdbcTemplate.queryForList("SHOW TABLES LIKE 'summary_%'", String.class);
//        jdbcTemplate.execute(String.format("CREATE TABLE summary_%d LIKE summary", archivedTables.size()));
//        jdbcTemplate.execute(String.format("INSERT summary_%d SELECT * FROM summary", archivedTables.size()));
    }

    public static void populateIndicators(JdbcTemplate jdbcTemplate, List<Indicators> indicators) {
        jdbcTemplate.update("DELETE FROM indicators");

        jdbcTemplate.batchUpdate("INSERT INTO indicators(time, short_ema, long_ema) VALUES(?, ?, ?)", indicators, 1000, (preparedStatement, indicator) -> {
            preparedStatement.setTimestamp(1, new Timestamp(indicator.getDate().getTime()));
            preparedStatement.setBigDecimal(2, indicator.getShortEMA());
            preparedStatement.setBigDecimal(3, indicator.getLongEMA());
        });
    }

    public static int countIndicators(JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.queryForObject("select count(*) from indicators", Integer.class);
    }

    public static void populatePositions(JdbcTemplate jdbcTemplate, List<Position> positions) {
        jdbcTemplate.update("DELETE FROM positions");

        jdbcTemplate.batchUpdate("INSERT INTO positions(open, close, max_profit, profit, open_date, close_date, duration) VALUES(?, ?, ?, ?, ?, ?, ?)", positions, 1000, (preparedStatement, position) -> {
            preparedStatement.setBigDecimal(1, position.getOpenPrice().getClose());
            preparedStatement.setBigDecimal(2, position.getClosePrice().getClose());
            preparedStatement.setBigDecimal(3, position.getMaxProfitPrice().getClose());
            preparedStatement.setBigDecimal(4, position.profit());
            preparedStatement.setTimestamp(5, new Timestamp(position.getOpenPrice().getDate().getTime()));
            preparedStatement.setTimestamp(6, new Timestamp(position.getClosePrice().getDate().getTime()));
            preparedStatement.setLong(7, Instant.ofEpochMilli(position.getOpenPrice().getDate().getTime()).until(Instant.ofEpochMilli(position.getClosePrice().getDate().getTime()), ChronoUnit.MINUTES));
        });
    }
}
