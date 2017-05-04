package by.fdf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author Dzmitry Fursevich
 */
@SpringBootApplication
public class DatabaseApp implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    public static void main(String[] args) {
        SpringApplication.run(DatabaseApp.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        Resource resource = new ClassPathResource("data/EURUSD_2016.csv");
        BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
        br.readLine();
        for (String line = br.readLine(); line != null; line = br.readLine()) {

        }
    }
}
