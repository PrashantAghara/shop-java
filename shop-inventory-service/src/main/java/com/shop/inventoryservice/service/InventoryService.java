package com.shop.inventoryservice.service;

import com.shop.inventoryservice.controller.dto.InventoryResponse;
import com.shop.inventoryservice.model.Inventory;
import com.shop.inventoryservice.repository.InventoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public List<InventoryResponse> isInStock(List<String> skuCode) {
        List<Inventory> inventories = inventoryRepository.findBySkuCodeIn(skuCode);
        log.info("skuCodes : {}", skuCode);
        log.info("Got Inventory of : " + inventories.size());
        List<InventoryResponse> inventoryResponses = new java.util.ArrayList<>(inventories.stream()
                .map(this::mapToResponse)
                .toList());

        List<String> gotSkuCodes = inventoryResponses.stream()
                .map(InventoryResponse::getSkuCode)
                .toList();

        skuCode.removeAll(gotSkuCodes);
        List<InventoryResponse> errorResponse = skuCode.stream()
                .map(this::mapErrorResponse)
                .toList();
        inventoryResponses.addAll(errorResponse);
        return inventoryResponses;
    }

    private InventoryResponse mapErrorResponse(String skuCode) {
        return InventoryResponse.builder()
                .skuCode(skuCode)
                .inStock(false)
                .build();
    }

    private InventoryResponse mapToResponse(Inventory inventory) {
        return InventoryResponse.builder()
                .skuCode(inventory.getSkuCode())
                .inStock(inventory.getQuantity() > 0)
                .build();
    }
}
