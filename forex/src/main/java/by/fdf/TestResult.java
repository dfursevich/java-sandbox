package by.fdf;

import org.springframework.util.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HOURS;

/**
 * @author Dzmitry Fursevich
 */
public class TestResult {
    private List<Position> positions = new ArrayList<>();

    public void append(Position position) {
        positions.add(position);
    }

    public int getTotalCount() {
        return positions.size();
    }

    public void print() {
        BigDecimal totalProfit = BigDecimal.ZERO;
        int profitCount = 0;
        int lossCount = 0;

        for (Position position : positions) {
            totalProfit = totalProfit.add(position.profit());
            if (position.profit().signum() >= 0) {
                profitCount = profitCount + 1;
            } else {
                lossCount = lossCount + 1;
            }
        }

        System.out.println("TestResult{" +
                "\ntotalProfit=" + totalProfit +
                ", \ntotalCount=" + positions.size() +
                ", \nprofitCount=" + profitCount +
                ", \nlossCount=" + lossCount +
                "\n}");
    }

    public void writeToFile(File file) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(file.toPath());
        writer.write("Open,Close,Profit,Open Date,Close Date,Duration");
        writer.newLine();
        DecimalFormat decimalFormat = new DecimalFormat("#.#####");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (Position position : positions) {
            writer.write(String.join(",", decimalFormat.format(position.getOpenPrice().getClose()),
                    decimalFormat.format(position.getClosePrice().getClose()),
                    decimalFormat.format(position.profit()),
                    dateFormat.format(position.getOpenPrice().getDate()),
                    dateFormat.format(position.getClosePrice().getDate()),
                    String.valueOf(HOURS.between(LocalDateTime.from(Instant.ofEpochMilli(position.getOpenPrice().getDate().getTime())),
                            LocalDateTime.from(Instant.ofEpochMilli(position.getClosePrice().getDate().getTime()))))));
            writer.newLine();
        }

        writer.close();
    }
}
