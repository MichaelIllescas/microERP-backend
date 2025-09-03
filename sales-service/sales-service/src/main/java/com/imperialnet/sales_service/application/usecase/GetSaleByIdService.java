package com.imperialnet.sales_service.application.usecase;

import com.imperialnet.sales_service.application.dto.SaleResponse;
import com.imperialnet.sales_service.application.port.in.GetSaleByIdUseCase;
import com.imperialnet.sales_service.application.port.out.SaleRepositoryPort;
import com.imperialnet.sales_service.domain.model.Sale;
import com.imperialnet.sales_service.infrastructure.mapper.SaleDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetSaleByIdService implements GetSaleByIdUseCase {

    private final SaleRepositoryPort saleRepositoryPort;
    private final SaleDtoMapper saleDtoMapper;

    @Override
    public SaleResponse getSaleById(Long id) {
        log.info("🔍 Buscando venta con ID: {}", id);

        Sale sale = saleRepositoryPort.findById(id)
                .orElseThrow(() -> {
                    log.warn("⚠️ No se encontró la venta con ID {}", id);
                    return new IllegalArgumentException("Venta no encontrada con ID: " + id);
                });

        log.info("✅ Venta encontrada con ID: {}", id);
        return saleDtoMapper.toResponse(sale);
    }
}
