package org.startup.productservice.controller;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private  List<String> products = new ArrayList<>(List.of("Product 1", "Product 2", "Product 3"));


    @GetMapping
    public List<String> getAllProducts() {
        return products;
    }

    @GetMapping("/{id}")
    public String getProductById(@PathVariable int id) {
        return products.get(id);
    }

    @PostMapping
    public String addProduct(@RequestBody String product) {
        products.add(product);
        return "Added: "+product;

    }
}
