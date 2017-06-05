package by.fdf;

import by.fdf.data.DataProvider;
import by.fdf.data.DataProviderImpl;
import by.fdf.data.DataProviderWrapper;
import by.fdf.domain.Position;
import by.fdf.domain.Summary;
import by.fdf.offset.LinearOffsetGenerator;
import by.fdf.runner.Runner;
import by.fdf.strategy.PositionStrategy;
import by.fdf.strategy.SimplePositionStrategy;
import by.fdf.util.SummaryBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
public class ForexApplicationTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void test() {
        DataProvider dataProvider = new DataProviderImpl(jdbcTemplate);

        Summary summary = Runner.run(
                () -> IntStream.range(0, 1).mapToObj(BigDecimal::valueOf),
                () -> IntStream.range(0, 1).mapToObj(BigDecimal::valueOf),
                (stopLoss, takeProfit) -> {
                    PositionStrategy strategy = new SimplePositionStrategy(stopLoss, takeProfit);

                    LinearOffsetGenerator offsetGenerator = new LinearOffsetGenerator();
                    DataProvider dataProviderWrapper = new DataProviderWrapper(dataProvider, (priceBar) -> {
                        offsetGenerator.next();
                    });
                    StrategyTester tester = new StrategyTester(1000000, strategy, dataProviderWrapper, offsetGenerator);

                    List<Position> positions = tester.runTest();

                    return new SummaryBuilder().setStopLoss(stopLoss).setTakeProfit(takeProfit).setPositions(positions).build();
                }
        ).get(0);

        BigDecimal profit = (BigDecimal) jdbcTemplate.queryForMap("SELECT sum(profit) AS profit FROM (SELECT @rn:=@rn+1 AS rank, (e2.close - e1.close) AS profit FROM eurusd e1 JOIN eurusd e2 ON e1.id + 1 = e2.id, (SELECT @rn:=0) t2) AS profit WHERE rank % 2 = 1;").get("profit");

        Assert.assertEquals(profit, summary.getProfit());
    }
}
