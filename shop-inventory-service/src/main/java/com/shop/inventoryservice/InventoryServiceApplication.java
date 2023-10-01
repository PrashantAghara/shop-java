package com.shop.inventoryservice;

import com.shop.inventoryservice.model.Inventory;
import com.shop.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
//        return args -> {
//            Inventory inventory = Inventory.builder()
//                    .skuCode("laptop")
//                    .quantity(100)
//                    .build();
//
//            Inventory inventory2 = Inventory.builder()
//                    .skuCode("iphone_13")
//                    .quantity(100)
//                    .build();
//
//            inventoryRepository.save(inventory);
//            inventoryRepository.save(inventory2);
//        };
//    }
}
