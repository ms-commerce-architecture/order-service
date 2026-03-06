package com.example.order_service.service;


import com.example.order_service.dto.request.OrderItemRequest;
import com.example.order_service.dto.request.OrderRequest;
import com.example.order_service.dto.response.OrderResponse;

import com.example.order_service.domain.model.Order;


import com.example.order_service.domain.repository.OrderRepository;
import com.example.order_service.event.publisher.OrderEventPublisher;
import com.example.order_service.exception.OrderNotFoundException;

import com.example.order_service.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigInteger;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;
    private final OrderEventPublisher eventPublisher;
    private final OrderMapper orderMapper;
    private final OrderItemService orderItemService;

    @Transactional
    public OrderResponse placeOrder(OrderRequest orderRequest) {
        List<OrderItemRequest> items = orderRequest.orderItemRequest();
        orderValidator.validateOrderItems(items);
        Order order = orderMapper.toOrder(orderRequest);
        Order saveOrder = orderRepository.save(order);
        orderItemService.createOrderItems(saveOrder.getOrderId(), items);
        eventPublisher.publishOrderCreated(order);
        return orderMapper.toOrderResponse(saveOrder);

    }


    @Transactional(readOnly = true)
    public OrderResponse getOrder(BigInteger orderId) {

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(
                "Order not found with id: " + orderId));
        return orderMapper.toOrderResponse(order);

    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderByOrderNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber).orElseThrow(() -> new OrderNotFoundException(
                "Order not found with id: " + orderNumber));
        return orderMapper.toOrderResponse(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByCustomer(BigInteger customerId) {
        List<Order> orders = orderRepository.findOrdersByCustomerId(customerId);
        return orders.stream().map(orderMapper::toOrderResponse).toList();
    }

}
