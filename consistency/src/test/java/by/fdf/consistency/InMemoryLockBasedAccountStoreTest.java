package by.fdf.consistency;

import org.junit.Before;
import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.Assert.*;

/**
 * @author Dzmitry Fursevich
 */
public class InMemoryLockBasedAccountStoreTest {

    private AccountStore accountStore;

    @Before
    public void setup() {
        accountStore = new InMemoryLockBasedAccountStore();
        accountStore.put("account-1", 1000L);
        accountStore.put("account-2", 1000L);
    }

    @Test
    public void test() {

        IntStream.range(0, 10).parallel().forEach(i ->
            IntStream.range(0, 1000)
        );

        accountStore.transfer("account-1", "account-2", 100L);

    }

}