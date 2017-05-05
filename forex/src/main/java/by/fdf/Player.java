package by.fdf;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;

/**
 * Created by Dmitry on 01.05.17.
 */
public class Player {
    private Strategy strategy;

    public Player(Strategy strategy) {
        this.strategy = strategy;
    }

    public void play() throws IOException {
        Resource resource = new ClassPathResource("data/EURUSD_2016.csv");
        BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
        br.readLine();
        for (String line = br.readLine(); line != null; line = br.readLine()) {
            BigDecimal close = new BigDecimal(line.split(",")[4]);
            strategy.tick(close);
        }
    }


}
