package org.startup.orderservice.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.startup.orderservice.dto.ProductDto;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final RestTemplate restTemplate;

    public OrderService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = "productServiceCB", fallbackMethod = "getProductFallback")
    public ProductDto getProductById(Long productId){
        String url="http://product-service/api/products/{productId}";
        return restTemplate.getForObject(url, ProductDto.class, productId);
    }

    public ProductDto getProductFallback(Long productId, Throwable t){
        log.error("Fallback for productId: {}, reason: {}", productId, t.getMessage());
        return new ProductDto(productId, "Unknown Product", 0.0, 0);
    }
}