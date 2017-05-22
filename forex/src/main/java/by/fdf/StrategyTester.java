package by.fdf;

import by.fdf.data.DataProvider;
import by.fdf.offset.OffsetGenerator;

import java.io.IOException;

/**
 * @author Dzmitry Fursevich
 */
public class StrategyTester {

    private int testCount;
    private PositionStrategy strategy;
    private DataProvider dataProvider;
    private OffsetGenerator offsetGenerator;

    public StrategyTester(int testCount, PositionStrategy strategy, DataProvider dataProvider, OffsetGenerator offsetGenerator) {
        this.testCount = testCount;
        this.strategy = strategy;
        this.dataProvider = dataProvider;
        this.offsetGenerator = offsetGenerator;
    }

    public void runTest(ResultCollector result) throws IOException {
        while (result.getTotalCount() < testCount) {
            dataProvider.setOffset(offsetGenerator.generate(dataProvider.totalRows()));
            PriceBar current = dataProvider.next();
            Position position = new Position(current, false);
            for (PriceBar bar = dataProvider.next(); bar != null; bar = dataProvider.next()) {
                if (strategy.close(position, bar)) {
                    position.close(bar);
                    break;
                }
            }

            if (position.isClosed()) {
                result.append(position);
            }
        }
    }
}
