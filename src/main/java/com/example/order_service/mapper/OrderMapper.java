package com.example.order_service.mapper;


import com.example.order_service.domain.model.Order;
import com.example.order_service.dto.request.OrderRequest;
import com.example.order_service.dto.response.OrderResponse;
import com.example.order_service.exception.OrderNotFoundException;

import org.mapstruct.*;

@Mapper(componentModel = "spring" ,unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface OrderMapper {

    @Named("mapToResponse")
    OrderResponse mapToResponse(Order order);

    @Mapping(target = "orderId", ignore = true)
    @Mapping(target = "orderNumber", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "orderStatus", constant = "PENDING")
    @Mapping(target = "paymentStatus", constant = "PENDING")
    @Mapping(target = "orderDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "paymentMethod", ignore = true)
    @Mapping(target = "deliveredDate", ignore = true)
    Order toOrder(OrderRequest request);


    default OrderResponse toOrderResponse(Order order) {
        if (order == null) {
            throw new OrderNotFoundException("Order cannot be null");
        }
        return mapToResponse(order);
    }

}