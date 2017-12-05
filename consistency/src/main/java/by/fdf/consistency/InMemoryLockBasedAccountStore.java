package by.fdf.consistency;

import com.google.common.collect.Sets;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * @author Dzmitry Fursevich
 */
public class InMemoryLockBasedAccountStore implements AccountStore {

    private Map<String, Long> data = new ConcurrentHashMap<>();
    private Map<String, ReentrantReadWriteLock> locks = new ConcurrentHashMap<>();

    @Override
    public void put(Map<String, Long> data) {
        //sort to avoid deadlocks
        Map<String, Long> sortedDataMap = new TreeMap<>(data);

        List<Lock> locks = sortedDataMap.entrySet().stream().map(entry -> getLock(entry.getKey()).writeLock()).collect(Collectors.toList());
        locks.forEach(Lock::lock);

        try {
            data.forEach((id, amount) -> this.data.put(id, amount));
        } finally {
            locks.forEach(Lock::unlock);
        }
    }

    @Override
    public Map<String, Long> get(Collection<String> ids) {
        //sort to avoid deadlocks
        Set<String> sortedIds = new TreeSet<>(ids);
        List<Lock> locks = sortedIds.stream().map(id -> getLock(id).readLock()).collect(Collectors.toList());
        locks.forEach(Lock::lock);

        try {
            return ids.stream().collect(Collectors.toMap(id -> id, id -> data.get(id)));
        } finally {
            locks.forEach(Lock::unlock);
        }
    }

    public void transfer(String fromId, String toId, Long amount) {
        //sort to avoid deadlocks
        Set<String> sortedIds = new TreeSet<>(Sets.newHashSet(fromId, toId));
        List<Lock> locks = sortedIds.stream().map(id -> getLock(id).readLock()).collect(Collectors.toList());
        locks.forEach(Lock::lock);

        try {
            Map<String, Long> accounts = sortedIds.stream().collect(Collectors.toMap(id -> id, id -> data.get(id)));
            Long fromAccount = accounts.get(fromId);
            if (fromAccount < amount) {
                throw new IllegalArgumentException("not enough money");
            }

            Long toAccount = accounts.get(toId);
            data.put(fromId, fromAccount - amount);
            data.put(toId, toAccount + amount);
        } finally {
            locks.forEach(Lock::unlock);
        }
    }

    private ReentrantReadWriteLock getLock(String id) {
        return locks.putIfAbsent(id, new ReentrantReadWriteLock());
    }

}
