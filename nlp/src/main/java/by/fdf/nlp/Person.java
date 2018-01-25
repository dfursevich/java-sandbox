package by.fdf.nlp;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * @author Dzmitry Fursevich
 */
@NodeEntity
public class Person {

    @GraphId
    private Long id;

    private String name;

    public Person() {
    }

    public Person(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
