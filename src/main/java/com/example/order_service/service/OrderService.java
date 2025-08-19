package com.example.order_service.service;


import com.example.order_service.client.InventeryClient;
import com.example.order_service.dto.OrderItemRequest;
import com.example.order_service.dto.OrderRequest;
import com.example.order_service.dto.OrderResponse;
import com.example.order_service.event.OrderEvent;
import com.example.order_service.model.Order;
import com.example.order_service.model.OrderItem;
import com.example.order_service.repository.OrderItemRepository;
import com.example.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventeryClient inventeryClient;
    private final OrderItemRepository orderItemRepository;
    private final KafkaTemplate<String,OrderEvent> kafkaTemplate;

    public OrderResponse placeOrder(OrderRequest orderRequest, List<OrderItemRequest> orderItemRequestList) {


       ValidItem(orderItemRequestList);



        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setCustomerId(orderRequest.customerId());
        order.setCustomerEmail(orderRequest.customerEmail());
        order.setCustomerName(orderRequest.customerName());
        order.setTotalAmount(orderRequest.totalAmount());
        order.setTaxAmount(orderRequest.taxAmount());
        order.setShippingAmount(orderRequest.shippingAmount());
        order.setDiscountAmount(orderRequest.discountAmount());

        order.setShippingAddress(orderRequest.shippingAddress());
        order.setBillingAddress(orderRequest.billingAddress());
        order.setShippedDate(orderRequest.shippedDate());
        Order saveOrder= orderRepository.save(order);
       List<OrderItem>  orderItems = createOrderItems(saveOrder.getOrderId(), orderItemRequestList);

       orderItemRepository.saveAll(orderItems);
       OrderEvent orderEvent = new OrderEvent(order.getOrderNumber(),orderRequest.customerEmail());
       log.info("Order event received",orderEvent);
       kafkaTemplate.send("order-event",orderEvent);
       log.info("End sending",orderEvent);

        return new OrderResponse(
                saveOrder.getOrderId(),
                saveOrder.getCustomerId(),
                saveOrder.getCustomerEmail(),
                saveOrder.getCustomerName(),
                saveOrder.getOrderStatus(),
                saveOrder.getTotalAmount(),
                saveOrder.getTaxAmount(),
                saveOrder.getShippingAmount(),
                saveOrder.getDiscountAmount(),
                saveOrder.getPaymentStatus(),
                saveOrder.getPaymentMethod(),
                saveOrder.getShippingAddress(),
                saveOrder.getBillingAddress(),
                saveOrder.getOrderDate(),
                saveOrder.getShippedDate(),
                saveOrder.getDeliveredDate()
        );

    }

    private List<OrderItem> createOrderItems(BigInteger orderId, List<OrderItemRequest> orderItemRequests) {
        return orderItemRequests.stream()
                .map(request -> {
                    OrderItem item = new OrderItem();
                    item.setOrderId(orderId);
                    item.setProductId(request.productId());
                    item.setProductName(request.productName());
                    item.setProductCode(request.productCode());
                    item.setQuantity(request.quantity());
                    item.setUnitPrice(request.unitPrice());
                    item.setTotalPrice(request.unitPrice().multiply(BigDecimal.valueOf(request.quantity())));
                    return item;
                })
                .collect(Collectors.toList());
    }

    private void ValidItem(List<OrderItemRequest> orderItemRequest) {
        for (OrderItemRequest orderItem : orderItemRequest) {
            boolean  checkOrderInStock=inventeryClient.IsInStock(orderItem.productId(),orderItem.quantity());
            if(!checkOrderInStock){  throw  new RuntimeException("there is no order in stock" + orderItem.productId());
            } }
    }


    public OrderResponse getOrder(BigInteger orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        return new OrderResponse(
                order.getOrderId(),
                order.getCustomerId(),
                order.getCustomerEmail(),
                order.getCustomerName(),
                order.getOrderStatus(),
                order.getTotalAmount(),
                order.getTaxAmount(),
                order.getShippingAmount(),
                order.getDiscountAmount(),
                order.getPaymentStatus(),
                order.getPaymentMethod(),
                order.getShippingAddress(),
                order.getBillingAddress(),
                order.getOrderDate(),
                order.getShippedDate(),
                order.getDeliveredDate()
        );
    }
    public OrderResponse getOrderByOrderNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber).orElse(null);
        return new OrderResponse(
                order.getOrderId(),
                order.getCustomerId(),
                order.getCustomerEmail(),
                order.getCustomerName(),
                order.getOrderStatus(),
                order.getTotalAmount(),
                order.getTaxAmount(),
                order.getShippingAmount(),
                order.getDiscountAmount(),
                order.getPaymentStatus(),
                order.getPaymentMethod(),
                order.getShippingAddress(),
                order.getBillingAddress(),
                order.getOrderDate(),
                order.getShippedDate(),
                order.getDeliveredDate()
        );
    }

    public List<OrderResponse> getOrdersByCustomer(BigInteger customerId) {
        List<Order> orders= orderRepository.findOrdersByCustomerId(customerId);
        return orders.stream().map(order -> new OrderResponse(
                order.getOrderId(),
                order.getCustomerId(),
                order.getCustomerEmail(),
                order.getCustomerName(),
                order.getOrderStatus(),
                order.getTotalAmount(),
                order.getTaxAmount(),
                order.getShippingAmount(),
                order.getDiscountAmount(),
                order.getPaymentStatus(),
                order.getPaymentMethod(),
                order.getShippingAddress(),
                order.getBillingAddress(),
                order.getOrderDate(),
                order.getShippedDate(),
                order.getDeliveredDate()
        )).collect(Collectors.toList());
    }


}
