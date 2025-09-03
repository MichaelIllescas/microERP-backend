package com.imperialnet.sales_service.application.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Builder
@Data
public class CreateSaleRequest {
    private Long customerId;
    private List<SaleItemRequest> items;
    private String paymentMethod;
}
