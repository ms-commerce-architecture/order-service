package com.example.order_service.dto.response;

import com.example.order_service.domain.model.Enums.OrderStatus;
import com.example.order_service.domain.model.Enums.PaymentStatus;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

public record OrderResponse(
                            BigInteger orderId,

                            BigInteger customerId,


                            String customerEmail,


                            String customerName,

                            OrderStatus orderStatus,

                            BigDecimal totalAmount,

                            BigDecimal taxAmount ,

                            BigDecimal shippingAmount ,

                            BigDecimal discountAmount ,


                            PaymentStatus paymentStatus ,


                            String paymentMethod,


                            String shippingAddress,


                            String billingAddress,


                            LocalDateTime orderDate,


                            LocalDateTime shippedDate,


                            LocalDateTime deliveredDate) {
}
