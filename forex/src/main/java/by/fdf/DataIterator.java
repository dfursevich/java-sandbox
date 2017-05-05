package by.fdf;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Dzmitry Fursevich
 */
public class DataIterator implements Closeable {
    private BufferedReader reader;

    public DataIterator(int position) throws IOException {
        reader = new BufferedReader(new InputStreamReader(new ClassPathResource("data/EURUSD_2016.csv").getInputStream()));
        reader.readLine();
        for(int i = 0; i < position; i++) {
            reader.readLine();
        }
    }

    public PriceBar readNext() throws IOException {
        String line = reader.readLine();
        if (line == null) {
            return null;
        }

        String[] splitLine = line.split(",");

        return new PriceBar();
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}
