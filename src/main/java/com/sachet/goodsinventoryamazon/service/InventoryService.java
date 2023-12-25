package com.sachet.goodsinventoryamazon.service;

import com.sachet.goodsinventoryamazon.model.Item;
import com.sachet.goodsinventoryamazon.repository.ItemsRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class InventoryService {

    private final ItemsRepository itemsRepository;

    public InventoryService(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    public Item saveItem(Item item) {
        Item savedItem = itemsRepository.save(item);
        return savedItem;
    }

    public Item addImage(MultipartFile file, String itemId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(itemId));
        List<Item> item = itemsRepository.get(query);
        Item requiredItem = item.get(0);
        /*
        Logic to save file to some remote location and add it to
        URL list in the Item document
         */
        return itemsRepository.save(requiredItem);
    }
}
