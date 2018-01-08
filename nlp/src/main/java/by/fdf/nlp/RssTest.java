package by.fdf.nlp;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.jsoup.Jsoup;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import java.net.URL;

/**
 * @author Dzmitry Fursevich
 */
public class RssTest implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(RssTest.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String url = "http://en.kremlin.ru/events/all/feed";
        SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(url)));
        System.out.println(feed);
        System.out.println(feed.getEntries().size());
        System.out.println(Jsoup.parse(feed.getEntries().get(1).getContents().get(0).getValue()).text());
    }
}
