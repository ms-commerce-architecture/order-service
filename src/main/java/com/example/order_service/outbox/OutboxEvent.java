package com.example.order_service.outbox;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;


@Entity
@Table(name = "t_order_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboxEvent {



    @Id
    private String id;

    @Column(nullable = false,length = 255)
    private  String aggregatetype;

    @Column(nullable = false,length = 255)
    private String aggregateid;

    @Column(nullable = false, length = 255)
    private  String type;

    @Column(nullable = false,columnDefinition = "JSON")
    private  String payload;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        if (id==null) {
            id = UUID.randomUUID().toString();
        }
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }

}
