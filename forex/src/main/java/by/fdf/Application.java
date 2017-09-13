package by.fdf;

import by.fdf.data.DataProvider;
import by.fdf.data.DataProviderImpl;
import by.fdf.data.DataProviderWrapper;
import by.fdf.domain.Indicators;
import by.fdf.domain.Position;
import by.fdf.domain.PriceBar;
import by.fdf.domain.Summary;
import by.fdf.offset.LinearOffsetGenerator;
import by.fdf.runner.Runner;
import by.fdf.strategy.DefaultPositionStrategy;
import by.fdf.strategy.PositionStrategy;
import by.fdf.ta4j.MovingMomentumStrategy;
import by.fdf.util.Database;
import by.fdf.util.SummaryBuilder;
import eu.verdelhan.ta4j.*;
import eu.verdelhan.ta4j.analysis.criteria.TotalProfitCriterion;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import eu.verdelhan.ta4j.indicators.trackers.EMAIndicator;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootApplication
public class Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... strings) throws Exception {
        populate();
        enrich();
//        analyseOne();
//        analyseTwo();
        analyseThree();
    }

    private void populate() throws IOException, ParseException {
        if (Database.countPrices(jdbcTemplate) > 0) {
            return;
        }

        System.out.println("Populating data...");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");

        BufferedReader br = new BufferedReader(new InputStreamReader(new ClassPathResource("data/EURUSD_2016_min.csv").getInputStream()));

        List<PriceBar> batch = new ArrayList<>();
        for (String line = br.readLine(); line != null; line = br.readLine()) {
            String[] tokens = line.split(",");
            PriceBar curr = new PriceBar(
                    dateFormat.parse(tokens[0] + " " + tokens[1]),
                    new BigDecimal(tokens[2]),
                    new BigDecimal(tokens[3]),
                    new BigDecimal(tokens[4]),
                    new BigDecimal(tokens[5]),
                    null);
            PriceBar prev = batch.size() != 0 ? batch.get(batch.size() - 1) : null;
            while (prev != null && prev.getDate().toInstant().until(curr.getDate().toInstant(), ChronoUnit.MINUTES) > 1) {
                prev = new PriceBar(
                        Date.from(prev.getDate().toInstant().plus(1, ChronoUnit.MINUTES)),
                        prev.getOpen(),
                        prev.getHigh(),
                        prev.getLow(),
                        prev.getClose(),
                        null);
                batch.add(prev);
            }
            batch.add(curr);
        }

        Database.populatePrices(jdbcTemplate, batch);

        System.out.println("Populating data... Finished.");
    }

    private void enrich() {
        if (Database.countIndicators(jdbcTemplate) > 0) {
            return;
        }

        System.out.println("Enriching data...");

        DataProvider dataProvider = new DataProviderImpl(jdbcTemplate);

        List<Tick> ticks = new ArrayList<>();

        Iterator<PriceBar> iterator = dataProvider.iterator(0);

        while (iterator.hasNext()) {
            PriceBar priceBar = iterator.next();
            Tick tick = new Tick(Period.seconds(60), new DateTime(priceBar.getDate()),
                    Decimal.valueOf(priceBar.getOpen().toString()),
                    Decimal.valueOf(priceBar.getHigh().toString()),
                    Decimal.valueOf(priceBar.getLow().toString()),
                    Decimal.valueOf(priceBar.getClose().toString()),
                    null);
            ticks.add(tick);
        }

        TimeSeries series = new TimeSeries(ticks);

        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);

        EMAIndicator shortSma = new EMAIndicator(closePrice, 12);
        EMAIndicator longSma = new EMAIndicator(closePrice, 25);

        List<Indicators> indicators = IntStream.range(series.getBegin(), series.getEnd() + 1).mapToObj(i ->
                new Indicators(series.getTick(i).getEndTime().toDate(),
                        new BigDecimal(shortSma.getValue(i).toString()),
                        new BigDecimal(longSma.getValue(i).toString()))).collect(Collectors.toList());

        Database.populateIndicators(jdbcTemplate, indicators);

        System.out.println("Enriching data... Finished.");
    }

    private void analyseOne() {
        DataProvider dataProvider = new DataProviderImpl(jdbcTemplate);

        List<Summary> summaries = Runner.run(
                () -> IntStream.range(0, 10).mapToObj(i -> BigDecimal.valueOf(i).divide(BigDecimal.valueOf(10000))),
                () -> IntStream.range(0, 10).mapToObj(i -> BigDecimal.valueOf(i).divide(BigDecimal.valueOf(10000))),
                (stopLoss, takeProfit) -> {
                    System.out.printf("Run test stopLoss=%s, takeProfit=%s\n", stopLoss, takeProfit);
//                    PositionStrategy strategy = new SimplePositionStrategy(stopLoss, takeProfit);
                    PositionStrategy strategy = new DefaultPositionStrategy(stopLoss, takeProfit);
//                    PositionStrategy strategy = new AutoClosePositionStrategy();

                    LinearOffsetGenerator offsetGenerator = new LinearOffsetGenerator();
                    DataProvider dataProviderWrapper = new DataProviderWrapper(dataProvider, (priceBar) -> {
                        offsetGenerator.next();
                    });
                    StrategyTester tester = new StrategyTester(1000000, strategy, dataProviderWrapper, offsetGenerator);

//                    OffsetGenerator offsetGenerator = new SequenceOffsetGenerator();
//                    StrategyTester tester = new StrategyTester(Integer.MAX_VALUE, strategy, dataProvider, offsetGenerator);

                    List<Position> positions = tester.runTest();

                    return new SummaryBuilder().setStopLoss(stopLoss).setTakeProfit(takeProfit).setPositions(positions).build();
                }
        );

        Database.populateSummaries(jdbcTemplate, summaries);
    }

    private void analyseTwo() {
        DataProvider dataProvider = new DataProviderImpl(jdbcTemplate);

        BigDecimal stopLoss = new BigDecimal("0.00090");
        BigDecimal takeProfit = new BigDecimal("0.00090");

        PositionStrategy strategy = new DefaultPositionStrategy(stopLoss, takeProfit);

        LinearOffsetGenerator offsetGenerator = new LinearOffsetGenerator();
        DataProvider dataProviderWrapper = new DataProviderWrapper(dataProvider, (priceBar) -> {
            offsetGenerator.next();
        });
        StrategyTester tester = new StrategyTester(1000000, strategy, dataProviderWrapper, offsetGenerator);

        List<Position> positions = tester.runTest();

        Database.populatePositions(jdbcTemplate, positions);

        Summary summary = new SummaryBuilder().setStopLoss(stopLoss).setTakeProfit(takeProfit).setPositions(positions).build();

        System.out.println(summary);
    }

    private void analyseThree() {
        DataProvider dataProvider = new DataProviderImpl(jdbcTemplate);

        List<Tick> ticks = new ArrayList<>();

        Iterator<PriceBar> iterator = dataProvider.iterator1h(0);

        while (iterator.hasNext()) {
            PriceBar priceBar = iterator.next();
            Tick tick = new Tick(Period.minutes(60), new DateTime(priceBar.getDate()),
                    Decimal.valueOf(priceBar.getOpen().toString()),
                    Decimal.valueOf(priceBar.getHigh().toString()),
                    Decimal.valueOf(priceBar.getLow().toString()),
                    Decimal.valueOf(priceBar.getClose().toString()),
                    null);
            ticks.add(tick);
        }

        TimeSeries series = new TimeSeries(ticks);

        // Building the trading strategy
        Strategy strategy = MovingMomentumStrategy.buildStrategy(series);

        // Running the strategy
        TradingRecord tradingRecord = series.run(strategy);
        System.out.println("Number of trades for the strategy: " + tradingRecord.getTradeCount());

        // Analysis
        System.out.println("Total profit for the strategy: " + new TotalProfitCriterion().calculate(series, tradingRecord));
    }
}
