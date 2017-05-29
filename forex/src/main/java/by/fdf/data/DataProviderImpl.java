package by.fdf.data;

import by.fdf.domain.PriceBar;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Dzmitry Fursevich
 */
public class DataProviderImpl implements DataProvider {
    private List<PriceBar> data = new ArrayList<>();

    public DataProviderImpl(JdbcTemplate jdbcTemplate) {
        data = jdbcTemplate.query("SELECT * FROM eurusd", (resultSet, i) -> new PriceBar(resultSet.getTimestamp(2), resultSet.getBigDecimal(3), resultSet.getBigDecimal(4), resultSet.getBigDecimal(5), resultSet.getBigDecimal(6)));
    }

    public int totalRows() {
        return data.size();
    }

    @Override
    public Iterator<PriceBar> iterator(int offset) {
        return new IteratorImpl(offset);
    }

    private class IteratorImpl implements Iterator<PriceBar> {
        private int offset;

        public IteratorImpl(int offset) {
            this.offset = offset;
        }

        @Override
        public boolean hasNext() {
            return offset < data.size();
        }

        @Override
        public PriceBar next() {
            PriceBar next = data.get(offset);

            offset = offset + 1;

            return next;
        }
    }
}
