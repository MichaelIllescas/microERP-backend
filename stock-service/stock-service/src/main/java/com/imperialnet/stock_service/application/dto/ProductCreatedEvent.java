package com.imperialnet.stock_service.application.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductCreatedEvent {
    private Long productId;
    private String sku;
    private String name;
    private Long categoryId;
    private Integer initialQuantity; // puede venir null
}
