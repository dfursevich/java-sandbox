package by.fdf;

import by.fdf.data.DataProvider;
import by.fdf.domain.Position;
import by.fdf.domain.PriceBar;
import by.fdf.offset.OffsetGenerator;
import by.fdf.strategy.PositionStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public List<Position> runTest() {
        List<Position> results = new ArrayList<>();
        while (results.size() < testCount) {
            Optional<Integer> offset = offsetGenerator.generate(dataProvider.totalRows());
            if (offset.isPresent()) {
                dataProvider.setOffset(offset.get());
                PriceBar current = dataProvider.next();
                Position position = new Position(current, false);
                for (PriceBar bar = dataProvider.next(); bar != null; bar = dataProvider.next()) {
                    if (strategy.close(position, bar)) {
                        position.close(bar);
                        break;
                    }
                }

                if (position.isClosed()) {
                    results.add(position);
                }
            } else {
                break;
            }
        }

        return results;
    }
}
