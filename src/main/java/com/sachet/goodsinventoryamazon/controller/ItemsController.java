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

    @PutMapping("/addImage/{itemId}")
    public ResponseEntity<String> addImage(@PathVariable String itemId,
                                         @RequestParam("file")MultipartFile file) throws Exception {
        return ResponseEntity.ok(inventoryService.addImage(file, itemId));
    }

    @GetMapping("/items/{userId}")
    public ResponseEntity<List<Item>> getAllItem(@PathVariable String userId) {
        return ResponseEntity.ok(inventoryService.getAllItem(userId));
    }
}
