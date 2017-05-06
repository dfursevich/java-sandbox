package by.fdf;

import java.io.IOException;
import java.util.Random;

/**
 * @author Dzmitry Fursevich
 */
public class StrategyTester {

    private int testCount;
    private PositionStrategy strategy;
    private DataProvider dataProvider;

    public StrategyTester(int testCount, PositionStrategy strategy, DataProvider dataProvider) {
        this.testCount = testCount;
        this.strategy = strategy;
        this.dataProvider = dataProvider;
    }

    public TestResult runTest() throws IOException {
        TestResult result = new TestResult();
        Random random = new Random();
        for (int i = 0; i < testCount; i++) {
            dataProvider.setOffset(random.nextInt(dataProvider.totalRows()));
            PriceBar current = dataProvider.next();
            Position position = new Position(current.getClose(), false);
            for (PriceBar bar = dataProvider.next(); bar != null; bar = dataProvider.next()) {
                if (strategy.close(position, bar)) {
                    position.close(bar.getClose());
                }
            }

            if (position.isClosed()) {
                result.append(position.profit());
            }
        }

        return result;
    }
}
