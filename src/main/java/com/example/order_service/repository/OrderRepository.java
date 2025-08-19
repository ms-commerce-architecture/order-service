package com.example.order_service.repository;


import com.example.order_service.model.Order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, BigInteger> {
    Optional<Order> findByOrderNumber(String orderNumber);
    List<Order> findOrdersByCustomerId(BigInteger customerId);
}
