package com.imperialnet.stock_service.application.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockResponse {
    private Long productId;
    private Integer quantity;
}
