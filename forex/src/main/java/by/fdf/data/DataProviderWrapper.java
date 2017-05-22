package by.fdf.data;

import by.fdf.PriceBar;

import java.io.IOException;
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
    public void setOffset(int offset) {
        dataProvider.setOffset(offset);
    }

    @Override
    public PriceBar next() throws IOException {
        PriceBar priceBar = dataProvider.next();
        consumer.accept(priceBar);
        return priceBar;
    }
}
