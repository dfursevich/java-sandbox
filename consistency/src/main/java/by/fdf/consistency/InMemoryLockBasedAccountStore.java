package by.fdf.consistency;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Dzmitry Fursevich
 */
public class InMemoryLockBasedAccountStore implements AccountStore {

    private Map<String, Long> data = new ConcurrentHashMap<>();
    private Map<String, ReentrantReadWriteLock> locks = new ConcurrentHashMap<>();

    @Override
    public void put(Map<String, Long> data) {

    }

    @Override
    public Long get(Collection<String> ids) {
        return null;
    }

    public void transfer(String fromId, String toId, Long amount) {

        Lock fromLock = getLock(fromId).writeLock();
        Lock toLock = getLock(toId).writeLock();

        fromLock.lock();
        try {
            Long fromAccount = get(fromId);
            if (fromAccount < amount) {
                throw new IllegalStateException("not enough money");
            }
            toLock.lock();
            try {
                Long toAccount = get(toId);
                data.put(fromId, fromAccount - amount);
                data.put(toId, toAccount + amount);
            } catch (Throwable e) {
                toLock.unlock();
            }
        } catch (Throwable e) {
            fromLock.unlock();
        }
    }

    private ReentrantReadWriteLock getLock(String id) {
        return locks.putIfAbsent(id, new ReentrantReadWriteLock());
    }
}
