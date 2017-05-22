package by.fdf.data;

import by.fdf.PriceBar;

import java.io.IOException;

/**
 * @author Dzmitry Fursevich
 */
public interface DataProvider {

    int totalRows();

    void setOffset(int offset);

    PriceBar next() throws IOException;
}
