package com.imperialnet.sales_service.application.usecase;

import com.imperialnet.sales_service.application.dto.SaleResponse;
import com.imperialnet.sales_service.application.port.in.ListSalesUseCase;
import com.imperialnet.sales_service.application.port.out.SaleRepositoryPort;
import com.imperialnet.sales_service.domain.model.Sale;
import com.imperialnet.sales_service.domain.model.SaleStatus;
import com.imperialnet.sales_service.infrastructure.mapper.SaleDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ListSalesService implements ListSalesUseCase {

    private final SaleRepositoryPort saleRepositoryPort;
    private final SaleDtoMapper saleDtoMapper;

    @Override
    public List<SaleResponse> listSales(String status) {
        List<Sale> sales;

        if (status == null) {
            log.info("📋 No se pasó parámetro de estado, usando por defecto CONFIRMED");
            sales = saleRepositoryPort.findByStatus(SaleStatus.CONFIRMED);
        } else {
            try {
                SaleStatus enumStatus = SaleStatus.valueOf(status.toUpperCase());
                log.info("📋 Buscando ventas con estado: {}", enumStatus);
                sales = saleRepositoryPort.findByStatus(enumStatus);
            } catch (IllegalArgumentException e) {
                log.warn("⚠️ Estado inválido recibido: '{}'. Se listarán todas las ventas.", status);
                sales = saleRepositoryPort.findAll();
            }
        }

        return sales.stream()
                .map(saleDtoMapper::toResponse)
                .collect(Collectors.toList());
    }
}
