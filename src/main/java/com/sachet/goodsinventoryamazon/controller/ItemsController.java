package com.sachet.goodsinventoryamazon.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sachet.goodsinventoryamazon.model.Item;
import com.sachet.goodsinventoryamazon.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/item")
public class ItemsController {

    private final InventoryService inventoryService;

    public ItemsController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/save")
    public ResponseEntity<Item> saveItem(@RequestPart("data") Item item, @RequestPart("file") MultipartFile file) throws Exception {
        return ResponseEntity.ok(inventoryService.saveItem(item, file));
    }

    @PostMapping("/update")
    public ResponseEntity<Item> updateItem(@RequestBody Item item) throws Exception {
        return ResponseEntity.ok(inventoryService.saveItem(item, null));
    }

    @PutMapping("/addImage/{itemId}")
    public ResponseEntity<Item> addImage(@PathVariable String itemId,
                                         @RequestParam("file")MultipartFile file) throws Exception {
        return ResponseEntity.ok(inventoryService.addImage(file, itemId));
    }

    @GetMapping("/items/{userId}")
    public ResponseEntity<List<Item>> getAllItem(@PathVariable String userId) {
        return ResponseEntity.ok(inventoryService.getAllItem(userId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Item>> getAllUserItem(@PathVariable String userId) {
        return ResponseEntity.ok(inventoryService.getAllItemOfUser(userId));
    }

    @GetMapping("/get/{itemId}")
    public ResponseEntity<Item> getItem(@PathVariable String itemId) throws Exception {
        return ResponseEntity.ok(inventoryService.findById(itemId));
    }
}
