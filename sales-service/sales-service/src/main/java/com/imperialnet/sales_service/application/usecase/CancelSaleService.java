package com.imperialnet.sales_service.application.usecase;

import com.imperialnet.sales_service.application.dto.SaleResponse;
import com.imperialnet.sales_service.application.port.in.CancelSaleUseCase;
import com.imperialnet.sales_service.application.port.out.SaleRepositoryPort;
import com.imperialnet.sales_service.domain.model.Sale;
import com.imperialnet.sales_service.domain.model.SaleStatus;
import com.imperialnet.sales_service.infrastructure.mapper.SaleDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CancelSaleService implements CancelSaleUseCase {

    private final SaleRepositoryPort saleRepositoryPort;
    private final SaleDtoMapper saleDtoMapper;

    @Override
    public SaleResponse cancelSale(Long id) {
        log.info("🔄 Solicitando cancelación de venta con ID {}", id);

        Sale sale = saleRepositoryPort.findById(id)
                .orElseThrow(() -> {
                    log.warn("⚠️ Venta no encontrada con ID {}", id);
                    return new IllegalArgumentException("Venta no encontrada con ID: " + id);
                });

        if (sale.getStatus() == SaleStatus.REJECTED) {
            log.warn("⚠️ La venta con ID {} ya está cancelada", id);
            throw new IllegalStateException("La venta ya está cancelada");
        }

        sale.setStatus(SaleStatus.REJECTED);
        Sale updated = saleRepositoryPort.save(sale);

        log.info("✅ Venta con ID {} cancelada correctamente", id);
        return saleDtoMapper.toResponse(updated);
    }
}
