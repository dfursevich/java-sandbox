package by.fdf.consistency;

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Dzmitry Fursevich
 */
public class InMemoryLockBasedAccountStoreTest {

    private AccountStore accountStore;
    private int accountCount = 10;
    private int accountAmount = 1000L;
    private int threadCount = 10;

    @Before
    public void setup() {

        accountStore = new InMemoryLockBasedAccountStore();

        IntStream.range(0, accountCount).forEach(i -> accountStore.put(ImmutableMap.of(String.valueOf(i), (long) accountAmount)));
    }

    @Test
    public void test() {
        IntStream.range(0, threadCount).parallel().forEach(i -> {
                    List<String> ids = IntStream.range(0, 10).mapToObj(String::valueOf).collect(Collectors.toList());
                    Collections.shuffle(ids);
                    accountStore.transfer(ids.get(0), ids.get(1), (long) new Random().nextInt(accountAmount));
                }
        );
    }

}