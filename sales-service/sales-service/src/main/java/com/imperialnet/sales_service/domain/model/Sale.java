package com.imperialnet.sales_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Sale {
    private Long id;
    private Long customerId;
    private Double totalAmount;
    private SaleStatus status;
    private LocalDateTime createdAt;
    private List<SaleItem> items;
}
