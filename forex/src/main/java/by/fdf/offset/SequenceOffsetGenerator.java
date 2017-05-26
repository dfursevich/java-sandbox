package by.fdf.offset;

import java.util.Optional;

/**
 * Created by dfursevich on 20.05.17.
 */
public class SequenceOffsetGenerator implements OffsetGenerator {

    private int counter = 0;

    @Override
    public Optional<Integer> generate(int bound) {
        if (counter == bound) {
            throw new IllegalStateException("generator is out of bounds");
        }

        return Optional.of(counter++);
    }
}
