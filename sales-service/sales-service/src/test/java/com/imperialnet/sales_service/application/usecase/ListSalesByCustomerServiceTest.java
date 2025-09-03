package com.imperialnet.sales_service.application.usecase;

import com.imperialnet.sales_service.application.dto.SaleResponse;
import com.imperialnet.sales_service.application.exception.SaleNotFoundException;
import com.imperialnet.sales_service.application.port.out.SaleRepositoryPort;
import com.imperialnet.sales_service.domain.model.Sale;
import com.imperialnet.sales_service.domain.model.SaleItem;
import com.imperialnet.sales_service.domain.model.SaleStatus;
import com.imperialnet.sales_service.infrastructure.mapper.SaleDtoMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListSalesByCustomerServiceTest {

    @Mock
    private SaleRepositoryPort saleRepositoryPort;

    private SaleDtoMapperImpl saleDtoMapper;

    @InjectMocks
    private ListSalesByCustomerService listSalesByCustomerService;

    private Sale sampleSale;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        saleDtoMapper = new SaleDtoMapperImpl();
        listSalesByCustomerService = new ListSalesByCustomerService(saleRepositoryPort, saleDtoMapper);

        sampleSale = Sale.builder()
                .id(1L)
                .customerId(100L)
                .totalAmount(200.0)
                .status(SaleStatus.CONFIRMED)
                .createdAt(LocalDateTime.of(2025, 1, 15, 10, 0))
                .items(List.of(
                        SaleItem.builder()
                                .productId(50L)
                                .quantity(2)
                                .price(100.0)
                                .productName("Producto X")
                                .build()
                ))
                .build();
    }

    @Test
    void testListSalesByCustomer_success() {
        when(saleRepositoryPort.findByCustomerId(100L)).thenReturn(List.of(sampleSale));

        List<SaleResponse> result = listSalesByCustomerService.listSalesByCustomer(100L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getSaleId());
        assertEquals(200.0, result.get(0).getTotalAmount());
        assertEquals("CONFIRMED", result.get(0).getStatus());

        verify(saleRepositoryPort, times(1)).findByCustomerId(100L);
    }

    @Test
    void testListSalesByCustomer_notFound() {
        when(saleRepositoryPort.findByCustomerId(200L)).thenReturn(List.of());

        SaleNotFoundException ex = assertThrows(
                SaleNotFoundException.class,
                () -> listSalesByCustomerService.listSalesByCustomer(200L)
        );

        assertTrue(ex.getMessage().contains("No se encontraron ventas para el cliente con ID=200"));

        verify(saleRepositoryPort, times(1)).findByCustomerId(200L);
    }
}
