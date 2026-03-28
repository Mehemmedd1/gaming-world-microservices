package org.startup.inventoryservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private Map<String, Integer> stock=new HashMap<>();

    public InventoryController() {
        stock.put("gaming-laptop", 10);
        stock.put("gaming-desktop", 10);
        stock.put("gaming-controller", 10);
    }
    @GetMapping
    public Map<String, Integer> getStock() {
        return stock;
    }
}
