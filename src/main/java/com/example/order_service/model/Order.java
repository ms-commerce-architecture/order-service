package com.example.order_service.model;


import com.example.order_service.model.Enums.OrderStatus;
import com.example.order_service.model.Enums.PaymentStatus;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
@Entity
@Table(name = "t_orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private BigInteger orderId;

    @Column(name = "order_number", nullable = false, unique = true, length = 50)
    private String orderNumber;

    @Column(name = "customer_id", nullable = false)
    private BigInteger customerId;

    @Column(name = "customer_email", nullable = false, length = 255)
    private String customerEmail;

    @Column(name = "customer_name", nullable = false, length = 255)
    private String customerName;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", columnDefinition = "ENUM('PENDING', 'CONFIRMED', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED') DEFAULT 'PENDING'")
    private OrderStatus orderStatus;

    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "tax_amount", precision = 10, scale = 2)
    private BigDecimal taxAmount ;

    @Column(name = "shipping_amount", precision = 10, scale = 2)
    private BigDecimal shippingAmount ;

    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount ;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", columnDefinition = "ENUM('PENDING', 'PAID', 'FAILED', 'REFUNDED') DEFAULT 'PENDING'")
    private PaymentStatus paymentStatus ;

    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Column(name = "shipping_address", nullable = false, columnDefinition = "json")
    private String shippingAddress;

    @Column(name = "billing_address", columnDefinition = "json")
    private String billingAddress;

    @Column(name = "order_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime orderDate;

    @Column(name = "shipped_date")
    private LocalDateTime shippedDate;

    @Column(name = "delivered_date")
    private LocalDateTime deliveredDate;
}