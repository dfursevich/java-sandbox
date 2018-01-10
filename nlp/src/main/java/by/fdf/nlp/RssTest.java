package by.fdf.nlp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Token;
import org.jsoup.Jsoup;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.core.io.ClassPathResource;

import java.util.Map;
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
                .flatMap(entry ->
                        new Document(Jsoup.parse(entry.getContents().get(0).getValue()).text()).sentences().stream()
                                .flatMap(sentence -> sentence.tokens().stream())
                                .filter(token -> "PERSON".equals(token.ner()))
                                .map(Token::word)
                                .collect(Collectors.toSet()).stream()
                )
                .collect(Collectors.toMap(token -> token, token -> 1, (v1, v2) -> v1 + v2));

        System.out.println(new ObjectMapper().writeValueAsString(persons));
    }
}
