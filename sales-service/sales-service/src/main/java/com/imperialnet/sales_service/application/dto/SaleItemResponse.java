package com.imperialnet.sales_service.application.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaleItemResponse {
    private Long productId;
    private Integer quantity;
    private Double price;
}
