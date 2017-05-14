package by.fdf;

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

        jdbcTemplate.update("delete from eurusd");

        Resource resource = new ClassPathResource("data/EURUSD_2016_min.csv");
        BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        for (String line = br.readLine(); line != null; line = br.readLine()) {
            String[] tokens = line.split(",");
            jdbcTemplate.update("insert into eurusd(time, open, high, low, close) values(?, ?, ?, ?, ?)",
                    dateFormat.parse(tokens[0] + " " + tokens[1]),
                    new BigDecimal(tokens[2]),
                    new BigDecimal(tokens[3]),
                    new BigDecimal(tokens[4]),
                    new BigDecimal(tokens[5]));
        }
    }
}
