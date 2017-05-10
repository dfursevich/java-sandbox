package by.fdf;

import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.util.Iterator;

/**
 * @author Dzmitry Fursevich
 */
public class DataProvider {

    private JdbcTemplate jdbcTemplate;
    private int offset;
    private Iterator<PriceBar> barIterator;

    public DataProvider(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int totalRows() {
        return jdbcTemplate.queryForObject("select count(*) from eurusd;", Integer.class);
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public PriceBar next() throws IOException {
        if (barIterator == null || !barIterator.hasNext()) {
            barIterator = jdbcTemplate.query("select * from eurusd limit ?, 1000", new Object[]{offset},
                    (resultSet, i) -> new PriceBar(resultSet.getTimestamp(2), resultSet.getBigDecimal(3), resultSet.getBigDecimal(4), resultSet.getBigDecimal(5), resultSet.getBigDecimal(6))).iterator();
        }

        PriceBar next = barIterator.hasNext() ? barIterator.next() : null;

        if (next != null) {
            offset = offset + 1;
        }

        return next;
    }
}
