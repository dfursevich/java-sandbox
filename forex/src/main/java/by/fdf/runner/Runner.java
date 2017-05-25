package by.fdf.runner;

import java.util.List;
import java.util.function.Function;
import java.util.stream.BaseStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Dzmitry Fursevich
 */
public class Runner {
    public static void run(Function<List<Object>, String> func, Stream<?>... streams) {}

    public static void main(String[] args) {
        Runner.run( params -> {
            Integer param1 = (Integer) params.get(0);
            Integer param2 = (Integer) params.get(1);
            return param1 + "_" + param2;
        }, IntStream.range(1, 10).boxed(), IntStream.range(10, 20).boxed());
    }
}
