package com.example.order_service.event.publisher;

import com.example.order_service.domain.model.Order;
import com.example.order_service.event.OrderEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventPublisher {
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public OrderEventPublisher(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void publishOrderCreated(Order order) {
        OrderEvent orderEvent=new OrderEvent(order.getOrderNumber(), order.getCustomerEmail());
        kafkaTemplate.send("order-event",orderEvent);
    }
}