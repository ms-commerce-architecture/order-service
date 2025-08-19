package com.example.order_service.dto;

import java.math.BigDecimal;


public record OrderItemRequest(


                               String productId,


                               String productName,


                               String productCode,


                               Integer quantity,


                               BigDecimal unitPrice,


                               BigDecimal totalPrice) {
}
