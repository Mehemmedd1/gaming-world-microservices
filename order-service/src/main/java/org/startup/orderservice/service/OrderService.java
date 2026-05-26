package org.startup.orderservice.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.startup.orderservice.dto.ProductDto;

@Service
public class OrderService {

    private final RestTemplate restTemplate;

    public OrderService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = "productServiceCB", fallbackMethod = "getProductFallback")
    @Retry(name = "productServiceRetry")
    public ProductDto getProductById(Long productId){
        String url="http://localhost:8081/api/products"+productId;
        return restTemplate.getForObject(url, ProductDto.class);
    }

    public ProductDto getProductFallback(Long productId,Throwable t){
        System.out.println("Fallback for productId: "+productId+", reason: "+t.getMessage());
        return new ProductDto(productId, "Unknown Product", 0.0);
    }
}
