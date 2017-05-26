package by.fdf.data;

import by.fdf.domain.PriceBar;

/**
 * @author Dzmitry Fursevich
 */
public interface DataProvider {

    int totalRows();

    void setOffset(int offset);

    PriceBar next();
}
