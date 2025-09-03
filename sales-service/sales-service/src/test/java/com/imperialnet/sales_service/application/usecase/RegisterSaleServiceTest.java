package com.imperialnet.sales_service.application.usecase;

import com.imperialnet.sales_service.application.dto.*;
import com.imperialnet.sales_service.application.port.out.*;
import com.imperialnet.sales_service.domain.model.Sale;
import com.imperialnet.sales_service.domain.model.SaleItem;
import com.imperialnet.sales_service.infrastructure.adapter.out.SaleEventPublisher;
import com.imperialnet.sales_service.infrastructure.mapper.SaleDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterSaleServiceTest {

    @Mock private SaleRepositoryPort saleRepositoryPort;
    @Mock private ProductServicePort productServicePort;
    @Mock private StockServicePort stockServicePort;
    @Mock private CustomerServicePort customerServicePort;
    @Mock private SaleDtoMapper saleDtoMapper;
    @Mock private SaleEventPublisher saleEventPublisher;

    @InjectMocks
    private RegisterSaleService registerSaleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ✅ Caso feliz
    @Test
    void registerSale_successful() {
      
    }

    // ❌ Cliente no encontrado
    @Test
    void registerSale_customerNotFound_throwsException() {
        // Arrange

    }

    // ❌ Stock insuficiente
    @Test
    void registerSale_insufficientStock_throwsException() {

    }
}
