package by.fdf.nlp;

import com.google.common.collect.Lists;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Dzmitry Fursevich
 */
@SpringBootApplication
public class NeoTest implements CommandLineRunner {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private PersonRepository personRepository;

    public static void main(String[] args) {
        SpringApplication.run(NeoTest.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        articleRepository.deleteAll();

//        String url = "http://en.kremlin.ru/events/all/feed";
        SyndFeed feed = new SyndFeedInput().build(new XmlReader(new ClassPathResource("kremlin.ru.xml").getFile()));
        List<Article> articles = feed.getEntries().stream().map(entry -> {
            Article article = new Article();
            article.setPublishDate(entry.getPublishedDate());

            List<Person> persons = NlpUtils.findPersons(entry.getContents().get(0).getValue()).stream()
                    .flatMap(person -> {
                        List<Person> existing = personRepository.findByName(person);
                        return (existing.isEmpty() ? Lists.newArrayList(new Person(null, person)) : existing).stream();
                    })
                    .collect(Collectors.toList());

            article.setPersons(persons);

            return article;
        }).collect(Collectors.toList());

        articles.forEach(article -> articleRepository.save(article));
    }
}
