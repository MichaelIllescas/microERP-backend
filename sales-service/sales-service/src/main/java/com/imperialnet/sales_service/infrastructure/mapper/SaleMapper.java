package com.imperialnet.sales_service.infrastructure.mapper;

import com.imperialnet.sales_service.domain.model.Sale;
import com.imperialnet.sales_service.domain.model.SaleItem;
import com.imperialnet.sales_service.infrastructure.persistence.entity.SaleEntity;
import com.imperialnet.sales_service.infrastructure.persistence.entity.SaleItemEntity;

public interface SaleMapper {
    SaleEntity toEntity(Sale sale);
    Sale toDomain(SaleEntity entity);
    SaleItemEntity toEntityItem(SaleItem item, SaleEntity parent);
    SaleItem toDomainItem(SaleItemEntity entity);
}
