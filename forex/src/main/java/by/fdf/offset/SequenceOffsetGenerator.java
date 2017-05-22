package by.fdf.offset;

/**
 * Created by dfursevich on 20.05.17.
 */
public class SequenceOffsetGenerator implements OffsetGenerator {

    private int counter = 0;

    @Override
    public int generate(int bound) {
        if (counter == bound) {
            throw new IllegalStateException("generator is out of bounds");
        }

        return counter++;
    }
}
