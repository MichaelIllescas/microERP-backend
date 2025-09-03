package com.imperialnet.sales_service.application.usecase;

import com.imperialnet.sales_service.application.dto.SaleResponse;
import com.imperialnet.sales_service.application.exception.SaleNotFoundException;
import com.imperialnet.sales_service.application.port.out.SaleRepositoryPort;
import com.imperialnet.sales_service.domain.model.Sale;
import com.imperialnet.sales_service.domain.model.SaleStatus;
import com.imperialnet.sales_service.infrastructure.mapper.SaleDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListSalesByDateRangeServiceTest {

    @Mock
    private SaleRepositoryPort saleRepositoryPort;

    @InjectMocks
    private ListSalesByDateRangeService listSalesByDateRangeService;

    private Sale sampleSale;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleSale = Sale.builder()
                .id(1L)
                .customerId(10L)
                .totalAmount(1500.0)
                .status(SaleStatus.CONFIRMED)
                .createdAt(LocalDateTime.of(2025, 1, 10, 12, 0))
                .items(List.of())
                .build();
    }

    @Test
    void testListSalesByDateRange_success() {
        LocalDate start = LocalDate.of(2025, 1, 1);
        LocalDate end = LocalDate.of(2025, 1, 31);

        // Crear una venta simulada
        Sale sampleSale = Sale.builder()
                .id(1L)
                .customerId(10L)
                .totalAmount(1500.0)
                .status(SaleStatus.CONFIRMED)
                .createdAt(LocalDateTime.of(2025, 1, 10, 12, 0))
                .items(List.of())
                .build();

        // Configurar mock para devolver la venta
        when(saleRepositoryPort.findByCreatedAtBetween(
                start.atStartOfDay(), end.atTime(23, 59, 59)))
                .thenReturn(List.of(sampleSale));

        // Ejecutar el caso de uso
        List<SaleResponse> result =
                listSalesByDateRangeService.listSalesByDateRange(start, end);

        // Validaciones
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1500.0, result.get(0).getTotalAmount());
        assertEquals(SaleStatus.CONFIRMED.name(), result.get(0).getStatus());

        // Verificar llamada al repositorio
        verify(saleRepositoryPort, times(1))
                .findByCreatedAtBetween(any(), any());
    }


    @Test
    void testListSalesByDateRange_noSalesFound() {
        LocalDate start = LocalDate.of(2025, 2, 1);
        LocalDate end = LocalDate.of(2025, 2, 28);

        when(saleRepositoryPort.findByCreatedAtBetween(
                start.atStartOfDay(), end.atTime(23, 59, 59)))
                .thenReturn(List.of()); // 👈 nunca null, siempre lista vacía

        SaleNotFoundException ex = assertThrows(
                SaleNotFoundException.class,
                () -> listSalesByDateRangeService.listSalesByDateRange(start, end)
        );

        assertTrue(ex.getMessage().contains("No se encontraron ventas"));
        verify(saleRepositoryPort, times(1))
                .findByCreatedAtBetween(any(), any());
    }
}
