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
    public ResponseEntity<Item> saveItem(@RequestBody Item item) throws JsonProcessingException {
        return ResponseEntity.ok(inventoryService.saveItem(item));
    }

    @PutMapping("/addImage/{itemId}")
    public ResponseEntity<Item> addImage(@PathVariable String itemId,
                                         @RequestParam("file")MultipartFile file) throws Exception {
        return ResponseEntity.ok(inventoryService.addImage(file, itemId));
    }

    @GetMapping("/test/all")
    public ResponseEntity<List<Item>> getAllItem() {
        return ResponseEntity.ok(inventoryService.getAllItem());
    }
}
