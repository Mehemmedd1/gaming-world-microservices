package org.startup.orderservice.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;
import org.startup.orderservice.entity.Order;
import org.startup.orderservice.repository.OrderRepository;
import org.startup.orderservice.config.RabbitMQConfig;


import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;

    public OrderController(OrderRepository orderRepository, RabbitTemplate rabbitTemplate) {
        this.orderRepository = orderRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping
    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        Order saved = orderRepository.save(order);
        rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_QUEUE, "Order created: " + saved.getId());
        return saved;
    }
}
