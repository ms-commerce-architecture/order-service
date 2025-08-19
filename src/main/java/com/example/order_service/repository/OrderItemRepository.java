package com.example.order_service.repository;

import com.example.order_service.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.math.BigInteger;

public interface OrderItemRepository extends JpaRepository<OrderItem, BigInteger> {
}
