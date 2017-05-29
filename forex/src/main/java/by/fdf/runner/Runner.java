package by.fdf.runner;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Dzmitry Fursevich
 */
public class Runner {
    public static <T, R> List<R> run(Supplier<Stream<T>> s, Function<T, R> func) {
        return s.get().parallel().map(func).collect(Collectors.toList());
    }

    public static <U, V, R> List<R>  run(Supplier<Stream<U>> s1, Supplier<Stream<V>> s2, BiFunction<U, V, R> func) {
        return s1.get().parallel().flatMap(a1 -> s2.get().map(a2 -> func.apply(a1, a2))).collect(Collectors.toList());
    }

    public static <U, V, S, R> List<R>  run(Supplier<Stream<U>> s1, Supplier<Stream<V>> s2, Supplier<Stream<S>> s3, TriFunction<U, V, S, R> func) {
        return s1.get().flatMap(a1 -> s2.get().flatMap(a2 -> s3.get().map(a3 -> func.apply(a1, a2, a3)))).collect(Collectors.toList());
    }

    public static <U, V, S, T, R> List<R>  run(Supplier<Stream<U>> s1, Supplier<Stream<V>> s2, Supplier<Stream<S>> s3, Supplier<Stream<T>> s4, FourFunction<U, V, S, T, R> func) {
        return s1.get().flatMap(a1 -> s2.get().flatMap(a2 -> s3.get().flatMap(a3 -> s4.get().map(a4 -> func.apply(a1, a2, a3, a4))))).collect(Collectors.toList());
    }

    @FunctionalInterface
    public interface TriFunction<T1, T2, T3, R> {
        R apply(T1 t1, T2 t2, T3 t3);
    }

    @FunctionalInterface
    public interface FourFunction<T1, T2, T3, T4, R> {
        R apply(T1 t1, T2 t2, T3 t3, T4 t4);
    }
}
