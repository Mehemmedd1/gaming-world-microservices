package org.startup.orderservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private List<String> orders = new ArrayList<>(List.of("Order1", "Order2", "Order3"));

    @GetMapping
    public List<String> getAllOrders(){
        return orders;
    }
}
