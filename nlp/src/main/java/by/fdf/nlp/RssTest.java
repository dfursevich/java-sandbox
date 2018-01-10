package by.fdf.nlp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Token;
import org.jsoup.Jsoup;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.core.io.ClassPathResource;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author Dzmitry Fursevich
 */
public class RssTest implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(RssTest.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        String url = "http://en.kremlin.ru/events/all/feed";
        SyndFeed feed = new SyndFeedInput().build(new XmlReader(new ClassPathResource("kremlin.ru.xml").getFile()));

        Map<String, Integer> persons = feed.getEntries().stream()
                .flatMap(entry -> new Document(Jsoup.parse(entry.getContents().get(0).getValue()).text()).sentences()
                        .stream()
                        .flatMap(sentence ->
                                sentence.tokens().stream()
                                        .collect(() -> Lists.<List<String>>newArrayList(Lists.<String>newArrayList()),
                                                (list, token) -> {
                                                    List<String> current = list.get(list.size() - 1);
                                                    if ("PERSON".equals(token.ner())) {
                                                        current.add(token.lemma());
                                                    } else if (!current.isEmpty()) {
                                                        list.add(Lists.newArrayList());
                                                    }
                                                }, (list1, list2) -> {
                                                    throw new UnsupportedOperationException();
                                                })
                                        .stream())
                        .filter(person -> !person.isEmpty())
                        .map(person -> person.stream().collect(Collectors.joining(" ")))
                        .collect(Collectors.toSet())
                        .stream())
                .collect(Collectors.toMap(person -> person, person -> 1, (v1, v2) -> v1 + v2))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        System.out.println(new ObjectMapper().writeValueAsString(persons));
    }
}
