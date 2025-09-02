package com.imperialnet.stock_service.application.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateStockRequest {
    private Integer quantity;
}
