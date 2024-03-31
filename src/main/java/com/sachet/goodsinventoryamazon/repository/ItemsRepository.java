package com.sachet.goodsinventoryamazon.repository;

import com.sachet.goodsinventoryamazon.model.Item;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ItemsRepository extends MongoRepository<Item, String> {
    List<Item> findByUserIdIsNot(String id);
    List<Item> findByUserId(String userId);
}
