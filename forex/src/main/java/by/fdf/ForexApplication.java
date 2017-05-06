package by.fdf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

//@SpringBootApplication
public class ForexApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ForexApplication.class, args);
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... strings) throws Exception {
        PositionStrategy strategy = new PositionStrategyImpl(new BigDecimal("0.001"), new BigDecimal("0.001"));
        DataProvider dataProvider = new DataProvider(jdbcTemplate);

        StrategyTester tester = new StrategyTester(1000, strategy, dataProvider);
        TestResult result = tester.runTest();
        System.out.println(result);
    }
}
