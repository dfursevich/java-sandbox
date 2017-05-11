package by.fdf;

import java.io.IOException;

/**
 * @author Dzmitry Fursevich
 */
public interface DataProvider {

    int totalRows();

    void setOffset(int offset);

    PriceBar next() throws IOException;
}
