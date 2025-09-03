package com.imperialnet.sales_service.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaleItem {
    private Long id;
    private Long productId;
    private String productName;
    private Integer quantity;
    private Double price;
}
