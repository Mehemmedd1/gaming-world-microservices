package org.startup.orderservice.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;
import org.startup.orderservice.dto.ProductDto;
import org.startup.orderservice.entity.Order;
import org.startup.orderservice.repository.OrderRepository;
import org.startup.orderservice.config.RabbitMQConfig;
import org.startup.orderservice.service.OrderService;


import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;
    private final OrderService orderService;

    public OrderController(OrderRepository orderRepository, RabbitTemplate rabbitTemplate, OrderService orderService) {
        this.orderRepository = orderRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }
    @GetMapping("/orders/product/{productId}")
    public ProductDto getProductInfo(@PathVariable Long productId) {
        return orderService.getProductById(productId);
    }


    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        Order saved = orderRepository.save(order);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_EXCHANGE,
                RabbitMQConfig.ORDER_ROUTING_KEY,
                "Order created: " + saved.getId());
        return saved;
    }
}

