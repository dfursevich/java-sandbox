package by.fdf.offset;

import java.util.Random;

/**
 * Created by dfursevich on 20.05.17.
 */
public class RandomOffsetGenerator implements OffsetGenerator {
    private Random random = new Random();

    @Override
    public int generate(int bound) {
        return random.nextInt(bound);
    }
}
