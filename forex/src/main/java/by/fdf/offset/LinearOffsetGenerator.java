package by.fdf.offset;

import java.util.Optional;
import java.util.Random;

/**
 * Created by dfursevich on 21.05.17.
 */
public class LinearOffsetGenerator implements OffsetGenerator {
    private int counter = 0;

    @Override
    public Optional<Integer> generate(int bound) {
        if (counter >= bound) {
            return Optional.empty();
        }

        return Optional.of(counter);
    }

    public void next() {
        counter++;
    }
}
