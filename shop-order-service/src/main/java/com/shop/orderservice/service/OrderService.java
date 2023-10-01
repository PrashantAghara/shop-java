package com.shop.orderservice.service;

import com.shop.orderservice.dto.InventoryResponse;
import com.shop.orderservice.dto.OrderLineItemsDTO;
import com.shop.orderservice.dto.OrderRequest;
import com.shop.orderservice.model.Order;
import com.shop.orderservice.model.OrderLineItems;
import com.shop.orderservice.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient webClient;

    @Autowired
    public OrderService(OrderRepository orderRepository, WebClient webClient) {
        this.orderRepository = orderRepository;
        this.webClient = webClient;
    }

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItems()
                .stream()
                .map(this::mapToOrderLineItems)
                .toList();
        order.setOrderLineItemsList(orderLineItemsList);
        List<String> skuCodes = orderRequest.getOrderLineItems()
                .stream()
                .map(OrderLineItemsDTO::getSkuCode)
                .toList();
        log.info("skuCodes : {}", skuCodes);
        InventoryResponse[] inventoryResponses = webClient.get()
                .uri("http://localhost:8082/api/inventory", uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();
        assert inventoryResponses != null;
        log.info("Inventories got : " + inventoryResponses.length);
        boolean allProductsInStock = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::getInStock);
        if (!allProductsInStock) {
            throw new IllegalArgumentException("Products are not in stock");
        }
        orderRepository.save(order);
        log.info("Order {} is placed", order.getId());
    }

    private OrderLineItems mapToOrderLineItems(OrderLineItemsDTO orderLineItemsDTO) {
        return new OrderLineItems(orderLineItemsDTO.getSkuCode(), orderLineItemsDTO.getPrice(), orderLineItemsDTO.getQuantity());
    }
}
