package com.imperialnet.sales_service.application.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class SaleResponse {
    private Long saleId;
    private Long customerId;
    private Double totalAmount;
    private String status;
    private List<SaleItemResponse> items;
    private LocalDateTime createdAt;
}
