package by.fdf.offset;

import java.util.Optional;

/**
 * Created by dfursevich on 20.05.17.
 */
public interface OffsetGenerator {
    Optional<Integer> generate(int bound);
}
