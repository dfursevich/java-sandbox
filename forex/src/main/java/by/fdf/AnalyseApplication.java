package by.fdf;

import by.fdf.data.DataProvider;
import by.fdf.data.DataProviderImpl;
import by.fdf.data.DataProviderWrapper;
import by.fdf.domain.Position;
import by.fdf.domain.Summary;
import by.fdf.offset.LinearOffsetGenerator;
import by.fdf.runner.Runner;
import by.fdf.strategy.AutoClosePositionStrategy;
import by.fdf.strategy.PositionStrategy;
import by.fdf.strategy.PositionStrategyImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootApplication
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = DatabaseApplication.class))
public class AnalyseApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(AnalyseApplication.class, args);
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... strings) throws Exception {
        jdbcTemplate.update("DELETE FROM summary");

        DataProvider dataProvider = new DataProviderImpl(jdbcTemplate);

        Runner.run(
                () -> IntStream.range(0, 1).mapToObj(i -> BigDecimal.valueOf(i).divide(BigDecimal.valueOf(10000))),
                () -> IntStream.range(0, 1).mapToObj(i -> BigDecimal.valueOf(i).divide(BigDecimal.valueOf(10000))),
                (stopLoss, takeProfit) -> {
                    System.out.printf("Run test stopLoss=%s, takeProfit=%s\n", stopLoss, takeProfit);
                    PositionStrategy strategy = new PositionStrategyImpl(stopLoss, takeProfit);
//                    PositionStrategy strategy = new AutoClosePositionStrategy();

                    LinearOffsetGenerator offsetGenerator = new LinearOffsetGenerator();
                    DataProvider dataProviderWrapper = new DataProviderWrapper(dataProvider, (priceBar) -> {
                        offsetGenerator.next();
                    });
                    StrategyTester tester = new StrategyTester(1000000, strategy, dataProviderWrapper, offsetGenerator);

                    List<Position> positions = tester.runTest();

                    BigDecimal profit = BigDecimal.ZERO;
                    int profitCount = 0;
                    int lossCount = 0;
                    for (Position position : positions) {
                        profit = profit.add(position.profit());
                        if (position.profit().signum() > 0) {
                            profitCount = profitCount + 1;
                        } else if (position.profit().signum() < 0) {
                            lossCount = lossCount + 1;
                        }
                    }

                    return new Summary(stopLoss, takeProfit, profit, positions.size(), profitCount, lossCount);
                }
        ).forEach(summary -> {
//            jdbcTemplate.update("INSERT INTO summary(stop_loss, take_profit, profit, total_count, profit_count, loss_count) VALUES(?, ?, ?, ?, ?, ?)",
//                    summary.getStopLoss(),
//                    summary.getTakeProfit(),
//                    summary.getProfit(),
//                    summary.getTotalCount(),
//                    summary.getProfitCount(),
//                    summary.getLossCount());

            System.out.println(summary);
        });
    }
}
