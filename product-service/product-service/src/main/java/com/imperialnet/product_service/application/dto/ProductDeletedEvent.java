package com.imperialnet.product_service.application.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDeletedEvent {
    private Long productId;
}
