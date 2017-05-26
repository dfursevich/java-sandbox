package by.fdf.offset;

import java.util.Optional;
import java.util.Random;

/**
 * Created by dfursevich on 20.05.17.
 */
public class RandomOffsetGenerator implements OffsetGenerator {
    private Random random = new Random();

    @Override
    public Optional<Integer> generate(int bound) {
        return Optional.of(random.nextInt(bound));
    }
}
