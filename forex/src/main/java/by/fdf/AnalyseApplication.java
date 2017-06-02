package by.fdf;

import by.fdf.data.DataProvider;
import by.fdf.data.DataProviderImpl;
import by.fdf.data.DataProviderWrapper;
import by.fdf.domain.Position;
import by.fdf.domain.Summary;
import by.fdf.offset.LinearOffsetGenerator;
import by.fdf.runner.Runner;
import by.fdf.strategy.PositionStrategy;
import by.fdf.strategy.PositionStrategyImpl;
import by.fdf.util.Database;
import by.fdf.util.SummaryBuilder;
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
        Database.clearSummary(jdbcTemplate);

        DataProvider dataProvider = new DataProviderImpl(jdbcTemplate);

        List<Summary> summaries = Runner.run(
                () -> IntStream.range(0, 10).mapToObj(i -> BigDecimal.valueOf(i).divide(BigDecimal.valueOf(10000))),
                () -> IntStream.range(0, 10).mapToObj(i -> BigDecimal.valueOf(i).divide(BigDecimal.valueOf(10000))),
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

                    return new SummaryBuilder().setStopLoss(stopLoss).setTakeProfit(takeProfit).setPositions(positions).build();
                }
        );

        Database.insertSummaryBatch(jdbcTemplate, summaries);
    }
}
