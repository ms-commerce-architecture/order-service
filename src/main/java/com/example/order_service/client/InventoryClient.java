package com.example.order_service.client;


import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;




public interface InventoryClient {
    Logger log = LoggerFactory.getLogger(InventoryClient.class);

    @GetExchange( "/api/ineventory")
    @CircuitBreaker(name = "ineventory",fallbackMethod = "fallbackMethod")
    @Retry(name = "ineventory")
    boolean isInStock(@RequestParam String productId, @RequestParam Integer quantity);


    default boolean fallbackMethod(String productId, Integer quantity,Throwable throwable) {
       log.error("fallbackMethod", throwable);
        return false;
    }
}
