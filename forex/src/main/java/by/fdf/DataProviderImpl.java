package by.fdf;

import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dzmitry Fursevich
 */
public class DataProviderImpl implements DataProvider {
    private int offset;
    private List<PriceBar> data = new ArrayList<>();

    public DataProviderImpl(JdbcTemplate jdbcTemplate) {
        data = jdbcTemplate.query("SELECT * FROM eurusd", (resultSet, i) -> new PriceBar(resultSet.getTimestamp(2), resultSet.getBigDecimal(3), resultSet.getBigDecimal(4), resultSet.getBigDecimal(5), resultSet.getBigDecimal(6)));
    }

    public int totalRows() {
        return data.size();
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public PriceBar next() throws IOException {
        PriceBar next = offset < data.size() ? data.get(offset) : null;

        if (next != null) {
            offset = offset + 1;
        }

        return next;
    }
}
