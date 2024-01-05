package com.sachet.goodsinventoryamazon.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sachet.goodsinventoryamazon.model.Item;
import com.sachet.goodsinventoryamazon.publisher.ItemCreatedPublisher;
import com.sachet.goodsinventoryamazon.repository.ItemsRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    private final ItemsRepository itemsRepository;
    private final ItemCreatedPublisher itemCreatedPublisher;

    public InventoryService(ItemsRepository itemsRepository, ItemCreatedPublisher itemCreatedPublisher) {
        this.itemsRepository = itemsRepository;
        this.itemCreatedPublisher = itemCreatedPublisher;
    }

    public Item saveItem(Item item) throws JsonProcessingException {
        Item savedItem = itemsRepository.save(item);
        itemCreatedPublisher.sendItemCreatedEvent(savedItem);
        return savedItem;
    }

    public Item addImage(MultipartFile file, String itemId) throws Exception {
        Optional<Item> item = itemsRepository.findById(itemId);
        if (item.isEmpty()) {
            throw new Exception("Cannot find item");
        }
        Item requiredItem = item.get();
        /*
        Logic to save file to some remote location and add it to
        URL list in the Item document
         */
        return itemsRepository.save(requiredItem);
    }

    public List<Item> getAllItem() {
        return itemsRepository.findAll();
    }
}
