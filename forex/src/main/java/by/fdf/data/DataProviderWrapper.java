package by.fdf.data;

import by.fdf.domain.PriceBar;

import java.io.IOException;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Created by dfursevich on 21.05.17.
 */
public class DataProviderWrapper implements DataProvider {
    private DataProvider dataProvider;
    private Consumer<PriceBar> consumer;

    public DataProviderWrapper(DataProvider dataProvider, Consumer<PriceBar> consumer) {
        this.dataProvider = dataProvider;
        this.consumer = consumer;
    }

    @Override
    public int totalRows() {
        return dataProvider.totalRows();
    }

    @Override
    public Iterator<PriceBar> iterator(int offset) {
        return wrap(dataProvider.iterator(offset));
    }

    @Override
    public Iterator<PriceBar> iterator5m(int offset) {
        return wrap(dataProvider.iterator5m(offset));
    }

    @Override
    public Iterator<PriceBar> iterator15m(int offset) {
        return wrap(dataProvider.iterator15m(offset));
    }

    @Override
    public Iterator<PriceBar> iterator30m(int offset) {
        return wrap(dataProvider.iterator30m(offset));
    }

    @Override
    public Iterator<PriceBar> iterator1h(int offset) {
        return wrap(dataProvider.iterator1h(offset));
    }

    private Iterator<PriceBar> wrap(final Iterator<PriceBar> iterator) {
        return new Iterator<PriceBar>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public PriceBar next() {
                PriceBar priceBar = iterator.next();
                consumer.accept(priceBar);
                return priceBar;
            }
        };
    }
}
