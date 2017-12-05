package by.fdf.consistency;

import java.util.Collection;
import java.util.Map;

/**
 * @author Dzmitry Fursevich
 */
public interface AccountStore {

    void put(Map<String, Long> data);

    Map<String, Long> get(Collection<String> ids);

    void transfer(String fromId, String toId, Long amount);

}
