package com.sachet.goodsinventoryamazon.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.sachet.goodsinventoryamazon.model.Item;
import com.sachet.goodsinventoryamazon.publisher.ItemCreatedPublisher;
import com.sachet.goodsinventoryamazon.repository.ItemsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryService.class);
    private final ItemsRepository itemsRepository;
    private final ItemCreatedPublisher itemCreatedPublisher;
    private final String bucketName;
    private final Storage storage;

    public InventoryService(ItemsRepository itemsRepository,
                            ItemCreatedPublisher itemCreatedPublisher,
                            Storage storage,
                            @Value("bucket_name")String bucketName) {
        this.itemsRepository = itemsRepository;
        this.itemCreatedPublisher = itemCreatedPublisher;
        this.storage = storage;
        this.bucketName = bucketName;
    }

    public Item saveItem(Item item) throws JsonProcessingException {
        Item savedItem = itemsRepository.save(item);
        itemCreatedPublisher.sendItemCreatedEvent(savedItem);
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
        try{
            String fileName = System.nanoTime() + file.getOriginalFilename();
            BlobId blobId = BlobId.of(bucketName, fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
            var storageCreate = storage.create(blobInfo, file.getBytes());
            List<String> imageURL = requiredItem.getImageURL();
            if (imageURL == null) {
                imageURL = new ArrayList<>();
            }
            imageURL.add(storageCreate.getName());
            requiredItem.setImageURL(imageURL);
            itemsRepository.save(requiredItem);
        }catch (IOException ex) {
            LOGGER.error("Exception while saving the item image {}", ex.getMessage());
            return "Unable to save Image "+ex.getMessage();
        }
        return "Image Uploaded";
    }

    public List<Item> getAllItem() {
        return itemsRepository.findAll();
    }
}
