package com.imperialnet.sales_service.infrastructure.mapper;

import com.imperialnet.sales_service.application.dto.CreateSaleRequest;
import com.imperialnet.sales_service.application.dto.SaleItemRequest;
import com.imperialnet.sales_service.application.dto.SaleItemResponse;
import com.imperialnet.sales_service.application.dto.SaleResponse;
import com.imperialnet.sales_service.domain.model.Sale;
import com.imperialnet.sales_service.domain.model.SaleItem;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Implementación manual del mapper. Mantiene el caso de uso limpio.
 */
@Component
public class SaleDtoMapperImpl implements SaleDtoMapper {

    @Override
    public Sale toDomain(CreateSaleRequest request) {
        if (request == null) return null;

        List<SaleItem> items = request.getItems() == null
                ? Collections.emptyList()
                : request.getItems().stream()
                .filter(Objects::nonNull)
                .map(this::toDomainItem)
                .collect(Collectors.toList());

        // Importante: price/total/status/createdAt se completan en el caso de uso.
        return Sale.builder()
                .customerId(request.getCustomerId())
                .items(items)
                .build();
    }

    private SaleItem toDomainItem(SaleItemRequest req) {
        return SaleItem.builder()
                .productId(req.getProductId())
                .quantity(req.getQuantity())
                // price se setea luego con ProductService
                .build();
    }

    @Override
    public SaleResponse toResponse(Sale sale) {
        if (sale == null) return null;

        List<SaleItemResponse> items = sale.getItems() == null
                ? Collections.emptyList()
                : sale.getItems().stream()
                .filter(Objects::nonNull)
                .map(i -> SaleItemResponse.builder()
                        .productId(i.getProductId())
                        .quantity(i.getQuantity())
                        .price(i.getPrice())
                        .build())
                .collect(Collectors.toList());

        return SaleResponse.builder()
                .saleId(sale.getId())
                .customerId(sale.getCustomerId())
                .totalAmount(sale.getTotalAmount())
                .status(sale.getStatus() != null ? sale.getStatus().name() : null)
                .items(items)
                .createdAt(sale.getCreatedAt())
                .build();
    }
}
