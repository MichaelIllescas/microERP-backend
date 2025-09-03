package com.imperialnet.sales_service.application.dto;

import lombok.Data;

@Data
public class StockResponse {
    private Long productId;
    private Integer quantity;
}
