package com.imperialnet.stock_service.domain.model;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class StockItem {
    private Long id;
    private Long productId;
    private Integer quantity;
    private String status; // ACTIVE/INACTIVE
}
