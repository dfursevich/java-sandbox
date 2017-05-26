package by.fdf.runner;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Dzmitry Fursevich
 */
public class Runner {
    public static <T> void run(Supplier<Stream<T>> s, Function<T, String> func) {
        s.get().map(func).forEach(System.out::println);
    }

    public static <U, V> void run(Supplier<Stream<U>> s1, Supplier<Stream<V>> s2, BiFunction<U, V, String> func) {
        s1.get().flatMap(a1 -> s2.get().map(a2 -> func.apply(a1, a2))).forEach(System.out::println);
    }

    public static <U, V, S> void run(Supplier<Stream<U>> s1, Supplier<Stream<V>> s2, Supplier<Stream<S>> s3, TriFunction<U, V, S, String> func) {
        s1.get().flatMap(a1 -> s2.get().flatMap(a2 -> s3.get().map(a3 -> func.apply(a1, a2, a3)))).forEach(System.out::println);
    }

    public static <U, V, S, T> void run(Supplier<Stream<U>> s1, Supplier<Stream<V>> s2, Supplier<Stream<S>> s3, Supplier<Stream<T>> s4, FourFunction<U, V, S, T, String> func) {
        s1.get().flatMap(a1 -> s2.get().flatMap(a2 -> s3.get().flatMap(a3 -> s4.get().map(a4 -> func.apply(a1, a2, a3, a4))))).forEach(System.out::println);
    }

    @FunctionalInterface
    public interface TriFunction<T1, T2, T3, R> {
        R apply(T1 t1, T2 t2, T3 t3);
    }

    @FunctionalInterface
    public interface FourFunction<T1, T2, T3, T4, R> {
        R apply(T1 t1, T2 t2, T3 t3, T4 t4);
    }

    public static void main(String[] args) {
        Runner.run(
                () -> IntStream.range(0, 2).boxed(),
                () -> IntStream.range(2, 4).boxed(),
                () -> IntStream.range(4, 6).boxed(),
                () -> IntStream.range(6, 8).boxed(),
                (a, b, c, d) -> a + "_" + b + "_" + c + "_" + d);
    }
}
