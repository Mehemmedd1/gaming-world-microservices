package org.startup.orderservice.repository;

import org.springframework.stereotype.Repository;
import org.startup.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
