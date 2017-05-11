package by.fdf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.math.BigDecimal;

@SpringBootApplication
public class AnalyseApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(AnalyseApplication.class, args);
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... strings) throws Exception {
        PositionStrategy strategy = new PositionStrategyImpl(new BigDecimal("0.001"), new BigDecimal("0.001"));
        DataProvider dataProvider = new DataProviderImpl(jdbcTemplate);
        StrategyTester tester = new StrategyTester(10000, strategy, dataProvider);
        TestResult result = tester.runTest();
        result.print();
        result.writeToFile(new File("positions.csv"));
    }
}




