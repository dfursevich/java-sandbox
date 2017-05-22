package by.fdf;

import by.fdf.data.DataProvider;
import by.fdf.data.DataProviderImpl;
import by.fdf.data.DataProviderWrapper;
import by.fdf.offset.LinearOffsetGenerator;
import by.fdf.offset.OffsetGenerator;
import by.fdf.offset.SequenceOffsetGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.math.BigDecimal;

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
        PositionStrategy strategy = new PositionStrategyImpl(new BigDecimal("0.0"), new BigDecimal("0.0000000000001"));
//        PositionStrategy strategy = new AutoClosePositionStrategy();
        DataProvider dataProvider = new DataProviderImpl(jdbcTemplate);
        LinearOffsetGenerator offsetGenerator = new LinearOffsetGenerator();
        DataProviderWrapper dataProviderWrapper = new DataProviderWrapper(dataProvider, (priceBar) -> {offsetGenerator.next();});
        ResultCollector result = new ResultCollector();
        StrategyTester tester = new StrategyTester(1000000, strategy, dataProviderWrapper, offsetGenerator);

        try {
            tester.runTest(result);
        } catch (IllegalStateException e) {
//            e.printStackTrace();
        }

        result.print();
        result.writeToFile(new File("data/positions.csv"));
    }
}



//totalProfit=-0.05740
//        totalCount=186339
//        profitCount=98671
//        lossCount=87668
//        (p - l)count =11003
