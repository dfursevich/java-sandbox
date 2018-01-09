package by.fdf.nlp;

import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.simple.Token;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.core.io.ClassPathResource;

import java.nio.file.Files;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by dfursevich on 09.01.18.
 */
public class StanfordNlpTest implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(StanfordNlpTest.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        String text = new String(Files.readAllBytes(new ClassPathResource("input.txt").getFile().toPath()));

        Document doc = new Document(text);
        for (Sentence sent : doc.sentences()) {  // Will iterate over two sentences
            System.out.println(sent.tokens().stream().map(Token::word).collect(Collectors.toList()));
            System.out.println(sent.nerTags());
        }
    }
}
