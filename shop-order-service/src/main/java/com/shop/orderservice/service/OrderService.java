package com.shop.orderservice.service;

import com.shop.orderservice.dto.OrderLineItemsDTO;
import com.shop.orderservice.dto.OrderRequest;
import com.shop.orderservice.model.Order;
import com.shop.orderservice.model.OrderLineItems;
import com.shop.orderservice.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItems()
                .stream()
                .map(this::mapToOrderLineItems)
                .toList();
        order.setOrderLineItemsList(orderLineItemsList);
        Order order1 = orderRepository.save(order);
        log.info("Product {} is saved", order1.getId());
    }

    private OrderLineItems mapToOrderLineItems(OrderLineItemsDTO orderLineItemsDTO) {
        return new OrderLineItems(orderLineItemsDTO.getSkuCode(), orderLineItemsDTO.getPrice(), orderLineItemsDTO.getQuantity());
    }
}
