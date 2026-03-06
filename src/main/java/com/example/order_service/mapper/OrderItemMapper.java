package com.example.order_service.mapper;

import com.example.order_service.domain.model.OrderItem;
import com.example.order_service.dto.request.OrderItemRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigInteger;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    @Mapping(target = "orderItemId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "totalPrice", expression = "java(request.unitPrice().multiply(java.math.BigDecimal.valueOf(request.quantity())))")
    OrderItem toOrderItem(OrderItemRequest request, BigInteger orderId);
}