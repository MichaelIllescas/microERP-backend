package com.imperialnet.sales_service.application.usecase;

import com.imperialnet.sales_service.application.dto.SaleResponse;
import com.imperialnet.sales_service.application.exception.SaleNotFoundException;
import com.imperialnet.sales_service.application.port.in.ListSalesByCustomerUseCase;
import com.imperialnet.sales_service.application.port.out.SaleRepositoryPort;
import com.imperialnet.sales_service.infrastructure.mapper.SaleDtoMapper;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ListSalesByCustomerService implements ListSalesByCustomerUseCase {

    private final SaleRepositoryPort saleRepositoryPort;
    private final SaleDtoMapper saleDtoMapper;

    @Override
    public List<SaleResponse> listSalesByCustomer(Long customerId) {
        log.info("📋 Listando ventas para el cliente con ID={}", customerId);
        List salesList = saleRepositoryPort.findByCustomerId(customerId)
                .stream()
                .map(saleDtoMapper::toResponse)
                .toList();

        if (salesList.isEmpty()) {
            log.warn("⚠️ No se encontraron ventas para el cliente con ID={}", customerId);
            throw new SaleNotFoundException("No se encontraron ventas para el cliente con ID=" + customerId);
        } else {
            log.info("✅ Se encontraron {} ventas para el cliente con ID={}", salesList.size(), customerId);
        }
        return salesList;
    }
}