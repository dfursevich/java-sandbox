package by.fdf.data;

import by.fdf.domain.PriceBar;

import java.util.Iterator;

/**
 * @author Dzmitry Fursevich
 */
public interface DataProvider {

    int totalRows();

    Iterator<PriceBar> iterator(int offset);

    Iterator<PriceBar> iterator5m(int offset);

    Iterator<PriceBar> iterator15m(int offset);

    Iterator<PriceBar> iterator30m(int offset);

    Iterator<PriceBar> iterator1h(int offset);
}
