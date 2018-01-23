package by.fdf.nlp;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Dzmitry Fursevich
 */
@Repository
public interface PersonRepository extends GraphRepository<Person> {
    List<Person> findByName(String name);
}
