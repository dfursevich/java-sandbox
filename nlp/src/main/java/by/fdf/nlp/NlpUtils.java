package by.fdf.nlp;

import com.google.common.collect.Lists;
import edu.stanford.nlp.simple.Document;
import org.jsoup.Jsoup;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Dzmitry Fursevich
 */
public class NlpUtils {
    public static Set<String> findPersons(String html) {
        return new Document(Jsoup.parse(html).text()).sentences()
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
                .collect(Collectors.toSet());

    }
}
