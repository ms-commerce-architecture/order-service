CREATE TABLE `t_orders` (
                            `order_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
                            `order_number` VARCHAR(50) NOT NULL UNIQUE,
                            `customer_id` BIGINT(20) NOT NULL,
                            `customer_email` VARCHAR(255) NOT NULL,
                            `customer_name` VARCHAR(255) NOT NULL,
                            `order_status` ENUM('PENDING', 'CONFIRMED', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED') DEFAULT 'PENDING',
                            `total_amount` DECIMAL(12,2) NOT NULL,
                            `tax_amount` DECIMAL(10,2) DEFAULT 0.00,
                            `shipping_amount` DECIMAL(10,2) DEFAULT 0.00,
                            `discount_amount` DECIMAL(10,2) DEFAULT 0.00,
                            `payment_status` ENUM('PENDING', 'PAID', 'FAILED', 'REFUNDED') DEFAULT 'PENDING',
                            `payment_method` VARCHAR(50),
                            `shipping_address` JSON NOT NULL,
                            `billing_address` JSON,
                            `order_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            `shipped_date` TIMESTAMP NULL,
                            `delivered_date` TIMESTAMP NULL,
                            `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            PRIMARY KEY (`order_id`)
);


CREATE TABLE `t_order_items` (
                                 `order_item_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
                                 `order_id` BIGINT(20) NOT NULL,
                                 `product_id` VARCHAR(255)  NOT NULL,
                                 `product_name` VARCHAR(255) NOT NULL,
                                 `product_code` VARCHAR(50) NOT NULL,
                                 `quantity` INT NOT NULL,
                                 `unit_price` DECIMAL(10,2) NOT NULL,
                                 `total_price` DECIMAL(12,2) NOT NULL,
                                 `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 PRIMARY KEY (`order_item_id`),
                                 FOREIGN KEY (`order_id`) REFERENCES `t_orders`(`order_id`) ON DELETE CASCADE
);


CREATE TABLE `t_order_status_history` (
                                          `history_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
                                          `order_id` BIGINT(20) NOT NULL,
                                          `old_status` VARCHAR(50),
                                          `new_status` VARCHAR(50) NOT NULL,
                                          `changed_by` VARCHAR(100),
                                          `change_reason` TEXT,
                                          `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                          PRIMARY KEY (`history_id`),
                                          FOREIGN KEY (`order_id`) REFERENCES `t_orders`(`order_id`) ON DELETE CASCADE
);
