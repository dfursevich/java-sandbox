package by.fdf;

import by.fdf.data.DataProvider;
import by.fdf.domain.Position;
import by.fdf.domain.PriceBar;
import by.fdf.offset.OffsetGenerator;
import by.fdf.strategy.PositionStrategy;

import java.util.ArrayList;
import java.util.Iterator;
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
                play(dataProvider.iterator(offset.get()), results);
            } else {
                break;
            }
        }

        return results;
    }

    private void play(Iterator<PriceBar> iterator, List<Position> results) {
        PriceBar current = iterator.next();
        Position position = new Position(current, false);
        for (; iterator.hasNext(); ) {
            PriceBar bar = iterator.next();
            position.update(bar);
            if (strategy.close(position, bar)) {
                position.close(bar);
                break;
            }
        }

        if (position.isClosed()) {
            results.add(position);
        }

//        List<Position> positions = new ArrayList<>();
//        for (; iterator.hasNext(); ) {
//            PriceBar bar = iterator.next();
//            positions.forEach(position -> position.update(bar));
//            positions.forEach(position -> {
//                if (strategy.close(position, bar)) {
//                    position.close(bar);
//                }
//            });
//            if (strategy.open(bar)) {
//                Position position = new Position(bar, false);
//                positions.add(position);
//            }
//        }
    }
}
