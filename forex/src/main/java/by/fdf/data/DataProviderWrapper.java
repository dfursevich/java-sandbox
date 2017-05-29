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
        Iterator<PriceBar> iterator = dataProvider.iterator(offset);
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
