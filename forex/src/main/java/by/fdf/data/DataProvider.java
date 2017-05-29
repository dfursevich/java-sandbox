package by.fdf.data;

import by.fdf.domain.PriceBar;

import java.util.Iterator;

/**
 * @author Dzmitry Fursevich
 */
public interface DataProvider {

    int totalRows();

    Iterator<PriceBar> iterator(int offset);
}
