package com.imperialnet.sales_service.application.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateSaleRequest {
    private Long customerId;
    private List<SaleItemRequest> items;
    private String paymentMethod;
}
