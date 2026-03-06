package com.example.order_service.config;

import com.example.order_service.client.InventoryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;


@Configuration
public class RestClientConfig {

    @Bean
    public InventoryClient inventoryClient() {
        RestClient restClient = RestClient.builder().baseUrl("http://localhost:8082").build();
        var resClientAdapter = RestClientAdapter.create(restClient);
        var httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(resClientAdapter).build();
        return  httpServiceProxyFactory.createClient(InventoryClient.class);
    }
}
