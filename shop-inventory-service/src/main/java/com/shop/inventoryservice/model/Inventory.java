package com.shop.inventoryservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "inventory")
public class Inventory {
    @Id
    private String id;
    private String skuCode;
    private Integer quantity;
}
