package com.sachet.goodsinventoryamazon.repository;

import com.sachet.goodsinventoryamazon.model.Item;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItemsRepository {

    private final MongoTemplate mongoTemplate;

    public ItemsRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Item save(Item item) {
        return mongoTemplate.save(item, Item.class.getName());
    }

    public List<Item> get(Query query) {
        return mongoTemplate.find(query, Item.class);
    }
}
