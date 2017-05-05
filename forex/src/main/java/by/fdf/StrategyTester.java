package by.fdf;

import java.io.IOException;
import java.util.Random;

/**
 * @author Dzmitry Fursevich
 */
public class StrategyTester {

    private int testCount;
    private Strategy strategy;

    public StrategyTester(int testCount, Strategy strategy) {
        this.testCount = testCount;
        this.strategy = strategy;
    }

    public void runTest() throws IOException {
        DataIterator iterator = new DataIterator(0);
        int total = 0;
        for (PriceBar priceBar = iterator.readNext(); priceBar != null; priceBar = iterator.readNext()) {
            total++;
        }

        new Random().ints(testCount, total).forEach(position -> {
//            new DataIterator(position)
        });

        for (int i = 0; i < testCount; i++) {
        }
    }
}
