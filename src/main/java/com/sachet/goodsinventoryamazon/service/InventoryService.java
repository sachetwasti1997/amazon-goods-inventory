package com.sachet.goodsinventoryamazon.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sachet.goodsinventoryamazon.config.AwsUtils;
import com.sachet.goodsinventoryamazon.model.Item;
import com.sachet.goodsinventoryamazon.publisher.ItemCreatedPublisher;
import com.sachet.goodsinventoryamazon.repository.ItemsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryService.class);
    private final ItemsRepository itemsRepository;
    private final ItemCreatedPublisher itemCreatedPublisher;
    private final AwsUtils awsUtils;
//    private final Storage storage;

    public InventoryService(ItemsRepository itemsRepository,
                            ItemCreatedPublisher itemCreatedPublisher,
                            AwsUtils awsUtils) {
        this.itemsRepository = itemsRepository;
        this.itemCreatedPublisher = itemCreatedPublisher;
        this.awsUtils = awsUtils;
    }

    public Item saveItem(Item item, MultipartFile file) throws Exception {
        Item savedItem = itemsRepository.save(item);
        itemCreatedPublisher.sendItemCreatedEvent(savedItem);
        if (file != null)
            addImage(file, savedItem.getId());
        return savedItem;
    }

    public String addImage(MultipartFile file, String itemId) throws Exception {
        Optional<Item> item = itemsRepository.findById(itemId);
        if (item.isEmpty()) {
            throw new Exception("Cannot find item");
        }
        Item requiredItem = item.get();
        /*
        Logic to save file to some remote location and add it to
        URL list in the Item document
         */
        String path = awsUtils.uploadFile(file);
        var list = requiredItem.getImageURL();
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(path);
        requiredItem.setImageURL(list);
        itemsRepository.save(requiredItem);
        return path;
    }

    public List<Item> getAllItem(String userId) {
        List<Item> items = itemsRepository.findAll();
        items = items.stream().filter(item -> item.getUserId() == null || !item.getUserId().equals(userId)).toList();
        return items;
    }
}
