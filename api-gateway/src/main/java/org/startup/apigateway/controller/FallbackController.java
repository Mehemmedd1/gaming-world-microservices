package org.startup.apigateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

    @GetMapping("/fallback/order")
    public Mono<String> orderFallback() {
        return Mono.just("Order service is currently unavailable");
    }
    @GetMapping("/fallback/product")
    public Mono<String> productFallback() {
        return Mono.just("Product service is currently unavailable");
    }
    @GetMapping("/fallback/user")
    public Mono<String> userFallback() {
        return Mono.just("User service is currently unavailable");
    }
    @GetMapping("/fallback/notification")
    public Mono<String> notificationFallback() {
        return Mono.just("Notification service is currently unavailable");
    }
    @GetMapping("/fallback/payment")
    public Mono<String> paymentFallback() {
        return Mono.just("Payment service is currently unavailable");
    }

}