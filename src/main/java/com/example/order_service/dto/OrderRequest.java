package com.example.order_service.dto;

import com.example.order_service.model.Enums.OrderStatus;
import com.example.order_service.model.Enums.PaymentStatus;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

public record OrderRequest(BigInteger customerId,


                           String customerEmail,


                           String customerName,



                           BigDecimal totalAmount,

                           BigDecimal taxAmount ,

                           BigDecimal shippingAmount ,

                           BigDecimal discountAmount ,








                           String shippingAddress,


                           String billingAddress,




                           LocalDateTime shippedDate,


                      List<OrderItemRequest> orderItemRequest) {

}
