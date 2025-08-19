package com.example.order_service.controller;


import com.example.order_service.dto.OrderRequest;
import com.example.order_service.dto.OrderResponse;
import com.example.order_service.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse placeOrder(@Valid @RequestBody OrderRequest request) {

            log.info("Placing order for customer: {}", request.customerEmail());
            return orderService.placeOrder(request, request.orderItemRequest());



    }

    @GetMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponse getOrder(@PathVariable BigInteger orderId) {

            OrderResponse orderResponse = orderService.getOrder(orderId);

return orderResponse;
    }

    @GetMapping("/number/{orderNumber}")
    @ResponseStatus(HttpStatus.OK)
    public  OrderResponse getOrderByNumber(@PathVariable String orderNumber) {

            OrderResponse orderResponse = orderService.getOrderByOrderNumber(orderNumber);
            return orderResponse;

    }

    @GetMapping("/customer/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponse> getCustomerOrders(@PathVariable BigInteger customerId) {

            List<OrderResponse> orders = orderService.getOrdersByCustomer(customerId);
            return orders;

    }
}

