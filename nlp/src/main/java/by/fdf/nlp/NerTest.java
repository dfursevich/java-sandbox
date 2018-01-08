package by.fdf.nlp;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * @author Dzmitry Fursevich
 */
public class NerTest implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(NerTest.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        String text = new String(Files.readAllBytes(new ClassPathResource("input.txt").getFile().toPath()));

        SentenceModel sentenceModel = null;
        try (InputStream modelIn = new FileInputStream(new ClassPathResource("en-sent.bin").getFile())) {
            sentenceModel = new SentenceModel(modelIn);
        }

        SentenceDetectorME sentenceDetector = new SentenceDetectorME(sentenceModel);
        String sentences[] = sentenceDetector.sentDetect(text);
        System.out.println(Arrays.toString(sentences));

        TokenizerModel tokenizerModel = null;
        try (InputStream modelIn = new FileInputStream(new ClassPathResource("en-token.bin").getFile())) {
            tokenizerModel = new TokenizerModel(modelIn);
        }

        Tokenizer tokenizer = new TokenizerME(tokenizerModel);
        String tokens[] = tokenizer.tokenize(text);
        System.out.println(Arrays.toString(tokens));

        TokenNameFinderModel model = null;
        try (InputStream modelIn = new FileInputStream(new ClassPathResource("en-ner-person.bin").getFile())) {
            model = new TokenNameFinderModel(modelIn);
        }

        NameFinderME nameFinder = new NameFinderME(model);
        Span nameSpans[] = nameFinder.find(tokens);
        System.out.println(Arrays.toString(nameSpans));
    }
}
