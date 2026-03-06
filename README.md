# Order Service

Spring Boot microservice for creating and querying customer orders. It persists order data in MySQL, validates stock through an inventory service client, and publishes order-created events to Kafka.

## Tech Stack

- Java 21
- Spring Boot 3.5
- Spring Data JPA + Flyway
- MySQL
- Spring Cloud OpenFeign / HTTP Interface style client
- Resilience4j (retry/circuit-breaker annotations)
- Spring Kafka
- springdoc OpenAPI (Swagger UI)

## Features

- Create orders (`POST /api/order`)
- Get order by ID (`GET /api/order/{orderId}`)
- Get order by order number (`GET /api/order/number/{orderNumber}`)
- Get all orders for a customer (`GET /api/order/customer/{customerId}`)
- Flyway-backed schema migration for orders, order items, and order status history
- Kafka event publishing on order creation

## Project Structure

```text
src/main/java/com/example/order_service
├── client          # External service clients (inventory)
├── config          # App configuration (REST client config)
├── controller      # REST API controllers
├── domain          # JPA entities + repositories
├── dto             # Request/response DTOs
├── event           # Domain events + publishers
├── exception       # Custom exceptions
├── mapper          # MapStruct mappers
└── service         # Business logic
```

## Prerequisites

- Java 21+
- Docker + Docker Compose
- (Optional) Maven installed globally, otherwise use `./mvnw`

## Local Run

1. Start infrastructure services:

   ```bash
   docker compose up -d
   ```

   This starts:
   - MySQL on `localhost:3306`
   - Kafka broker on `localhost:9092`
   - Kafka UI on `http://localhost:8086`

2. Start the application:

   ```bash
   ./mvnw spring-boot:run
   ```

3. Service will be available at:
   - API base URL: `http://localhost:8081/api/order`
   - Swagger UI: `http://localhost:8081/swagger-ui.html`
   - OpenAPI JSON: `http://localhost:8081/v3/api-docs`

## API Examples

### Create Order

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

### Get Order by ID

```bash
curl http://localhost:8081/api/order/1
```

### Get Order by Number

```bash
curl http://localhost:8081/api/order/number/ORD-123456
```

### Get Orders by Customer

```bash
curl http://localhost:8081/api/order/customer/1001
```

## Configuration

Main application settings live in:

- `src/main/resources/application.properties`
- `docker-compose.yml`
- `src/main/resources/db/migration/V1__init.sql`

Default local values include:

- App port: `8081`
- MySQL URL: `jdbc:mysql://localhost:3306/order_service`
- Kafka bootstrap servers: `localhost:9092`

## Build & Test

```bash
./mvnw clean test
```

## Notes

- The service expects an inventory endpoint for stock checks through `InventoryClient`.
- On successful order creation, an order event is published to Kafka.
