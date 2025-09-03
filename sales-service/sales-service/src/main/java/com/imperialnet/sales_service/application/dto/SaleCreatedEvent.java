package com.imperialnet.sales_service.application.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class SaleCreatedEvent {
    private Long saleId;
    private Double totalAmount;
    private LocalDateTime createdAt;

    // Datos del cliente
    private Long customerId;
    private String customerName;
    private String customerEmail;

    // Productos vendidos
    private List<SaleItemEvent> items;
}
