package org.startup.productservice.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.startup.productservice.entity.Product;
import org.startup.productservice.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private static final String PRODUCTS_CACHE = "products";
    private static final String PRODUCT_BY_ID_CACHE = "productById";

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Cacheable(PRODUCTS_CACHE)
    public List<Product> getAllProducts() {

        return productRepository.findAll();
    }

    @Cacheable(value = PRODUCT_BY_ID_CACHE, key = "#id")
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @CacheEvict(value = PRODUCTS_CACHE, allEntries = true)
    public void addProduct(Product product) {
        productRepository.save(product);
    }

    @Caching(
            put = @CachePut(value = PRODUCT_BY_ID_CACHE, key = "#id", unless = "#result == null"),
            evict = @CacheEvict(value = PRODUCTS_CACHE, allEntries = true)
    )
    public Product updateProduct(Long id, Product product) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getName() != null) {
            existingProduct.setName(product.getName());
        }
        if (product.getPrice() != null) {
            existingProduct.setPrice(product.getPrice());
        }
        if (product.getStock() != null) {
            existingProduct.setStock(product.getStock());
        }

        return productRepository.save(existingProduct);
    }

    @Caching(evict = {
            @CacheEvict(value = PRODUCT_BY_ID_CACHE, key = "#id"),
            @CacheEvict(value = PRODUCTS_CACHE, allEntries = true)
    })
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}

