package com.example.bidding.controller;

import com.example.bidding.domain.dto.ItemDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class ItemsDataController {
    @GetMapping(value = "/items")
    public ResponseEntity<ItemDto> getItemsData() {
        return ResponseEntity.ok(null);
    }

    @GetMapping(value = "/items/{itemName}")
    public ResponseEntity<ItemDto> getItemData(@PathVariable String itemName) {
        return ResponseEntity.ok(null);
    }
    @PostMapping(value = "/create")
    public ResponseEntity<ItemDto> createItem(@RequestBody ItemDto itemDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

}
