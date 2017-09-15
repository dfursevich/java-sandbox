package by.fdf.data;

import by.fdf.domain.Indicators;
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
        data = jdbcTemplate.query("SELECT * FROM eurusd p LEFT JOIN indicators i ON p.time = i.time",
                (resultSet, i) -> new PriceBar(resultSet.getTimestamp(2), resultSet.getBigDecimal(3), resultSet.getBigDecimal(4), resultSet.getBigDecimal(5), resultSet.getBigDecimal(6),
                        new Indicators(resultSet.getTimestamp(8), resultSet.getBigDecimal(9), resultSet.getBigDecimal(10))));
    }

    public int totalRows() {
        return data.size();
    }

    @Override
    public Iterator<PriceBar> iterator(int offset) {
        return new IteratorImpl(offset, 1);
    }

    @Override
    public Iterator<PriceBar> iterator5m(int offset) {
        return new IteratorImpl(offset, 5);
    }

    @Override
    public Iterator<PriceBar> iterator15m(int offset) {
        return new IteratorImpl(offset, 15);
    }

    @Override
    public Iterator<PriceBar> iterator30m(int offset) {
        return new IteratorImpl(offset, 30);
    }

    @Override
    public Iterator<PriceBar> iterator1h(int offset) {
        return new IteratorImpl(offset, 60);
    }

    private class IteratorImpl implements Iterator<PriceBar> {
        private int offset;
        private int period;

        public IteratorImpl(int offset, int period) {
            this.offset = offset;
            this.period = period;
        }

        @Override
        public boolean hasNext() {
            return offset < data.size();
        }

        @Override
        public PriceBar next() {
            PriceBar next = data.get(offset);

            offset = offset + period;

            return next;
        }
    }
}
