package by.fdf;

import by.fdf.domain.PriceBar;
import by.fdf.util.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dzmitry Fursevich
 */
@SpringBootApplication
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = AnalyseApplication.class))
public class DatabaseApplication implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(DatabaseApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("Running database application");

        Database.clearPrice(jdbcTemplate);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");

        BufferedReader br = new BufferedReader(new InputStreamReader(new ClassPathResource("data/EURUSD_2016_min.csv").getInputStream()));

        List<PriceBar> batch = new ArrayList<>();
        for (String line = br.readLine(); line != null; line = br.readLine()) {
            String[] tokens = line.split(",");
            batch.add(new PriceBar(
                    dateFormat.parse(tokens[0] + " " + tokens[1]),
                    new BigDecimal(tokens[2]),
                    new BigDecimal(tokens[3]),
                    new BigDecimal(tokens[4]),
                    new BigDecimal(tokens[5])));
        }

        System.out.println("Start inserting to database");

        Database.insertPriceBatch(jdbcTemplate, batch);
    }
}
