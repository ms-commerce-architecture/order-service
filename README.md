# Order Service

Spring Boot microservice for creating and retrieving customer orders. The current implementation includes MySQL persistence, stock validation, Kafka event publication, and an outbox pattern for downstream event-driven integrations.

## Overview

This service is built around the order lifecycle:

- accepts a customer order request
- validates each product item against an inventory service
- persists the complete order and order items in MySQL
- creates an outbox event entry for `OrderCreated`
- publishes the event to Kafka
- exposes query endpoints for retrieval by ID, order number, and customer

## Tech Stack

- Java 21
- Spring Boot 3.5
- Spring Data JPA
- Flyway database migrations
- MySQL
- OpenFeign / HTTP interface client
- Resilience4j retry + circuit breaker
- Spring Kafka
- springdoc OpenAPI / Swagger UI
- Docker Compose for local infrastructure

## Implemented Features

- Create an order via `POST /api/order`
- Read an order by ID via `GET /api/order/{orderId}`
- Read an order by order number via `GET /api/order/number/{orderNumber}`
- Read all orders for a customer via `GET /api/order/customer/{customerId}`
- Validate stock before accepting an order via `InventoryClient`
- Serialize and store `OrderCreated` events in the outbox table `t_order_events`
- Publish Kafka events with Spring Kafka
- Expose interactive API docs through Swagger UI

## Project Structure

```text
src/main/java/com/example/order_service
├── client               # External inventory client and fallback logic
├── config               # HTTP client + framework configuration
├── controller           # REST API endpoints
├── domain
│   ├── model            # Order, items, status history, enums
│   └── repository       # JPA repositories
├── dto                  # Request and response DTOs
├── event                # Order event payload and Kafka publisher
├── exception            # Domain exceptions
├── mapper               # MapStruct mappers
├── outbox               # Outbox event entity and persistence layer
├── service              # Business logic and validation
└── OrderServiceApplication.java
```

## Local Infrastructure

The project includes a Docker Compose setup for the supporting services:

- MySQL on `localhost:3306`
- Kafka broker on `localhost:9092`
- ZooKeeper (required for Kafka)
- Kafka UI on `http://localhost:8086`
- Debezium Connect on `http://localhost:8083`
- Schema Registry on `http://localhost:8085`

Project files:

- `docker-compose.yml`
- `debezium/order-outbox-connector.json`
- `docker/mysql/init.sql`

## Prerequisites

- Java 21+
- Docker and Docker Compose
- Maven wrapper included (`./mvnw`) or a global Maven installation

## Quick Start

1. Start the required infrastructure:

   ```bash
   docker compose up -d
   ```

2. Start the application:

   ```bash
   ./mvnw spring-boot:run
   ```

3. Open the API docs:

   - Swagger UI: `http://localhost:8081/swagger-ui.html`
   - Swagger UI (new path): `http://localhost:8081/swagger-ui/index.html`
   - OpenAPI JSON: `http://localhost:8081/v3/api-docs`

## API Examples

### Create order

```bash
curl -X POST http://localhost:8081/api/order \
  -H 'Content-Type: application/json' \
  -d '{
    "customerId": 1001,
    "customerEmail": "john@example.com",
    "customerName": "John Doe",
    "totalAmount": 149.99,
    "taxAmount": 10.00,
    "shippingAmount": 5.00,
    "discountAmount": 0.00,
    "shippingAddress": "{\"line1\":\"123 Main St\",\"city\":\"Austin\"}",
    "billingAddress": "{\"line1\":\"123 Main St\",\"city\":\"Austin\"}",
    "shippedDate": null,
    "orderItemRequest": [
      {
        "productId": "SKU-100",
        "productName": "Wireless Mouse",
        "productCode": "MOUSE-01",
        "quantity": 1,
        "unitPrice": 149.99,
        "totalPrice": 149.99
      }
    ]
  }'
```

### Get order by ID

```bash
curl http://localhost:8081/api/order/1
```

### Get order by order number

```bash
curl http://localhost:8081/api/order/number/ORD-123456
```

### Get orders by customer

```bash
curl http://localhost:8081/api/order/customer/1001
```

## Configuration

Main application settings are stored in:

- `src/main/resources/application.properties`
- `docker-compose.yml`
- Flyway migrations under `src/main/resources/db/migration`

Default local configuration values include:

- application port: `8081`
- database: `jdbc:mysql://localhost:3306/order_service`
- Kafka bootstrap servers: `localhost:9092`
- default Kafka topic: `order-event`

## Build and Test

```bash
./mvnw clean test
```

## Notes

- The service validates inventory via `InventoryClient` and uses resilience settings to retry and fail safely when the external stock service is unavailable.
- The `t_order_events` table is used as an outbox store so order events can be captured by Debezium and distributed to downstream systems.
- For the order creation flow, the application creates the order, stores outbox entries, and emits Kafka events after persistence.
