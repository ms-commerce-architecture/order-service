package com.example.order_service.service;

import com.example.order_service.domain.model.OrderItem;
import com.example.order_service.domain.repository.OrderItemRepository;
import com.example.order_service.dto.request.OrderItemRequest;
import com.example.order_service.mapper.OrderItemMapper;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    public OrderItemService(OrderItemRepository orderItemRepository, OrderItemMapper orderItemMapper) {
        this.orderItemRepository = orderItemRepository;
        this.orderItemMapper = orderItemMapper;
    }

    public void createOrderItems(BigInteger orderId, List<OrderItemRequest> requests) {
        List<OrderItem>  orderItems = requests.stream()
                .map(request -> orderItemMapper.toOrderItem(request, orderId))
                .toList();
        orderItemRepository.saveAll(orderItems);
    }
}