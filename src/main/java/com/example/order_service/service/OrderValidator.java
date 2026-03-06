package com.example.order_service.service;


import com.example.order_service.client.InventoryClient;
import com.example.order_service.dto.request.OrderItemRequest;
import com.example.order_service.exception.InsufficientStockException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderValidator {
    private final InventoryClient inventoryClient;

    public OrderValidator(InventoryClient inventoryClient) {
        this.inventoryClient = inventoryClient;
    }


    public void validateOrderItems(List<OrderItemRequest> orderItemRequests) {
        for (OrderItemRequest orderItem : orderItemRequests) {
            boolean checkOrderInStock = inventoryClient.isInStock(
                    orderItem.productId(),
                    orderItem.quantity()
            );

            if (!checkOrderInStock) {
                throw new InsufficientStockException(
                        "Product " + orderItem.productId() + " has insufficient stock for quantity " + orderItem.quantity()
                );
            }
        }
    }
}
