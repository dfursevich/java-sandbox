package by.fdf;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.IntStream;

@SpringBootApplication
public class ForexApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ForexApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
//        Map<BigDecimal, BigDecimal> results = new LinkedHashMap<>();
//        for (int i = 1; i <= 100; i++) {
//            BigDecimal stopLoss = new BigDecimal(i).divide(new BigDecimal(10000));
//            Strategy strategy = new SimpleStrategy(stopLoss);
//            new Player(strategy).play();
//            results.put(stopLoss, strategy.getTotal());
//        }

        Strategy strategy = new SimpleStrategy(new BigDecimal("0.0001"), new BigDecimal("0.0001"));
        new Player(strategy).play();

        strategy.print();
    }
}
