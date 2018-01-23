package by.fdf.nlp;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Dzmitry Fursevich
 */
@Repository
public interface ArticleRepository extends GraphRepository<Article> {
}
