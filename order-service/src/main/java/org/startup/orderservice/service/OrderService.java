package org.startup.orderservice.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.startup.orderservice.dto.ProductDto;

@Service
@Slf4j
public class OrderService {

    private final RestTemplate restTemplate;

    public OrderService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Retry(name = "productServiceRetry")
    @CircuitBreaker(name = "productServiceCB", fallbackMethod = "getProductFallback")
    public ProductDto getProductById(Long productId){
        log.info(">>> CƏHD: Məhsul ID={}", productId);
        System.out.println(">>> CƏHD: Məhsul ID=" + productId);
        String url="http://product-service/api/products/{productId}";
        return restTemplate.getForObject(url, ProductDto.class, productId);
    }

    public ProductDto getProductFallback(Long productId, Throwable t){
        log.error("Fallback for productId: {}, reason: {}", productId, t.getMessage());
        return new ProductDto(productId, "Unknown Product", 0.0, 0);
    }
}