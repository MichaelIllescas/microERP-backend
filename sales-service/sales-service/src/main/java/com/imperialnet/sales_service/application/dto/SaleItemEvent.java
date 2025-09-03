package com.imperialnet.sales_service.application.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaleItemEvent {
    private Long productId;
    private String productName;
    private Integer quantity;
    private Double price;
}
