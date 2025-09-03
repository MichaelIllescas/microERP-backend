package com.imperialnet.sales_service.infrastructure.mapper;

import com.imperialnet.sales_service.domain.model.Sale;
import com.imperialnet.sales_service.domain.model.SaleItem;
import com.imperialnet.sales_service.domain.model.SaleStatus;
import com.imperialnet.sales_service.infrastructure.persistence.entity.SaleEntity;
import com.imperialnet.sales_service.infrastructure.persistence.entity.SaleItemEntity;
import com.imperialnet.sales_service.infrastructure.persistence.entity.SaleStatusEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SaleMapperImpl implements SaleMapper {

    @Override
    public SaleEntity toEntity(Sale sale) {
        if (sale == null) return null;

        SaleEntity entity = SaleEntity.builder()
                .id(sale.getId())
                .customerId(sale.getCustomerId())
                .totalAmount(sale.getTotalAmount())
                .status(SaleStatusEntity.valueOf(sale.getStatus().name()))
                .createdAt(sale.getCreatedAt())
                .build();

        if (sale.getItems() != null) {
            List<SaleItemEntity> items = sale.getItems().stream()
                    .map(item -> toEntityItem(item, entity))
                    .collect(Collectors.toList());
            entity.setItems(items);
        }

        return entity;
    }

    @Override
    public Sale toDomain(SaleEntity entity) {
        if (entity == null) return null;

        Sale sale = Sale.builder()
                .id(entity.getId())
                .customerId(entity.getCustomerId())
                .totalAmount(entity.getTotalAmount())
                .status(SaleStatus.valueOf(entity.getStatus().name()))
                .createdAt(entity.getCreatedAt())
                .build();

        if (entity.getItems() != null) {
            List<SaleItem> items = entity.getItems().stream()
                    .map(this::toDomainItem)
                    .collect(Collectors.toList());
            sale.setItems(items);
        }

        return sale;
    }

    @Override
    public SaleItemEntity toEntityItem(SaleItem item, SaleEntity parent) {
        return SaleItemEntity.builder()
                .id(item.getId())
                .productId(item.getProductId())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .sale(parent)
                .build();
    }

    @Override
    public SaleItem toDomainItem(SaleItemEntity entity) {
        return SaleItem.builder()
                .id(entity.getId())
                .productId(entity.getProductId())
                .quantity(entity.getQuantity())
                .price(entity.getPrice())
                .build();
    }
}
