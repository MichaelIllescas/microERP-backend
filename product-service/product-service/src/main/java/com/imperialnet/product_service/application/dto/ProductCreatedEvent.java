package com.imperialnet.product_service.application.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCreatedEvent {
    private Long productId;
    private String name;
    private Long categoryId;
    private Integer initialQuantity;
}

