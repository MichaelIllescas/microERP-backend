package com.imperialnet.sales_service.infrastructure.mapper;

import com.imperialnet.sales_service.application.dto.CreateSaleRequest;
import com.imperialnet.sales_service.application.dto.SaleResponse;
import com.imperialnet.sales_service.domain.model.Sale;

/**
 * Mapper entre DTOs de aplicación y el modelo de dominio.
 * No resuelve precios ni total: eso lo hace el caso de uso.
 */
public interface SaleDtoMapper {

    /** Convierte el request de creación en un agregado de dominio (sin precios, total ni status). */
    Sale toDomain(CreateSaleRequest request);

    /** Convierte el agregado de dominio en la respuesta DTO. */
    SaleResponse toResponse(Sale sale);
}
