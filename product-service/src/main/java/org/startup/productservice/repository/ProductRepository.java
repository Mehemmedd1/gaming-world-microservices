package org.startup.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.startup.productservice.entity.Product;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
