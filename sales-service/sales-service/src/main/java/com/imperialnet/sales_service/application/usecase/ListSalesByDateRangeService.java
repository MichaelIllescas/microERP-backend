package com.imperialnet.sales_service.application.usecase;

import com.imperialnet.sales_service.application.dto.SaleResponse;
import com.imperialnet.sales_service.application.port.in.ListSalesByDateRangeUseCase;
import com.imperialnet.sales_service.application.port.out.SaleRepositoryPort;
import com.imperialnet.sales_service.domain.model.Sale;
import com.imperialnet.sales_service.infrastructure.mapper.SaleDtoMapper;
import com.imperialnet.sales_service.application.exception.SaleNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ListSalesByDateRangeService implements ListSalesByDateRangeUseCase {

    private final SaleRepositoryPort saleRepositoryPort;
    private final SaleDtoMapper saleDtoMapper;

    @Override
    public List<SaleResponse> listSalesByDateRange(LocalDate startDate, LocalDate endDate) {
        log.info("📋 Buscando ventas entre {} y {}", startDate, endDate);

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);

        List<Sale> sales = Optional.ofNullable(
                saleRepositoryPort.findByCreatedAtBetween(start, end)
        ).orElse(List.of());

        if (sales.isEmpty()) {
            log.warn("⚠️ No se encontraron ventas en el rango {} - {}", startDate, endDate);
            throw new SaleNotFoundException(
                    "No se encontraron ventas entre " + startDate + " y " + endDate
            );
        }

        log.info("✅ Se encontraron {} ventas en el rango {} - {}", sales.size(), startDate, endDate);
        return sales.stream().map(saleDtoMapper::toResponse).toList();
    }
}
