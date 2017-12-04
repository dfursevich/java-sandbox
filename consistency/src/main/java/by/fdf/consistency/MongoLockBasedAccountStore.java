package by.fdf.consistency;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Dzmitry Fursevich
 */
@Component
public class MongoLockBasedAccountStore implements AccountStore {

    private MongoTemplate mongoTemplate;

    public MongoLockBasedAccountStore(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void put(String id, Long amount) {
        mongoTemplate.save(new Account(id, amount, new Date(), false, null));
    }

    @Override
    public Long get(String id) {
        return mongoTemplate.findById(id, Account.class).getAmount();
    }

    @Override
    public void transfer(String fromId, String toId, Long amount) {
        acquireLock(fromId);
        try {
            acquireLock(toId);
            try {
                updateAccount(fromId, -1 * amount);
                updateAccount(toId, amount);
            } finally {
                releaseLock(toId);
            }
        } finally {
            releaseLock(fromId);
        }
    }

    private void updateAccount(String id, Long amount) {
        Query query = Query.query(Criteria.where("_id").is(id));

        Update update = new Update()
                .inc("amount", amount)
                .set("timestamp", new Date());

        this.mongoTemplate.updateFirst(query, update, Account.class);
    }

    private void acquireLock(String id) {
        Query query = Query.query(Criteria.where("_id").is(id).andOperator(Criteria.where("locked").is(false)));

        Update update = new Update()
                .set("locked", true)
                .set("lockDate", new Date());

        this.mongoTemplate.updateFirst(query, update, Account.class);
    }

    private void releaseLock(String id) {
        Query query = Query.query(Criteria.where("_id").is(id));

        Update update = new Update()
                .set("locked", false)
                .set("lockDate", null);

        this.mongoTemplate.updateFirst(query, update, Account.class);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Document
    private static class Account {

        private String id;
        private Long amount;
        private Date timestamp;
        private Boolean locked;
        private Date lockedDate;

    }

}
