package com.imperialnet.sales_service.application.usecase;

import com.imperialnet.sales_service.application.dto.*;
import com.imperialnet.sales_service.application.port.out.CustomerServicePort;
import com.imperialnet.sales_service.application.port.out.ProductServicePort;
import com.imperialnet.sales_service.application.port.out.SaleRepositoryPort;
import com.imperialnet.sales_service.application.port.out.StockServicePort;
import com.imperialnet.sales_service.domain.model.Sale;
import com.imperialnet.sales_service.domain.model.SaleItem;
import com.imperialnet.sales_service.domain.model.SaleStatus;
import com.imperialnet.sales_service.infrastructure.adapter.out.SaleEventPublisher;
import com.imperialnet.sales_service.infrastructure.mapper.SaleDtoMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterSaleServiceTest {

    @Mock
    private SaleRepositoryPort saleRepositoryPort;

    @Mock
    private ProductServicePort productServicePort;

    @Mock
    private StockServicePort stockServicePort;

    @Mock
    private CustomerServicePort customerServicePort;

    @Mock
    private SaleEventPublisher saleEventPublisher;

    private SaleDtoMapperImpl saleDtoMapper;

    private RegisterSaleService registerSaleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        saleDtoMapper = new SaleDtoMapperImpl();
        registerSaleService = new RegisterSaleService(
                saleRepositoryPort,
                productServicePort,
                stockServicePort,
                saleDtoMapper,
                customerServicePort,
                saleEventPublisher
        );
    }

    @Test
    void testRegisterSale_success() {
        // Arrange
        CreateSaleRequest request = CreateSaleRequest.builder()
                .customerId(1L)
                .items(List.of(
                        SaleItemRequest.builder().productId(100L).quantity(2).build()
                ))
                .build();

        CustomerResponse customer = new CustomerResponse();
        customer.setId(1L);
        customer.setFirstName("Juan");
        customer.setLastName("Pérez");
        customer.setEmail("juan@test.com");
        ProductResponse product = new ProductResponse();
        product.setId(100L);
        product.setName("Laptop");
        product.setPrice(1500.0);

        when(customerServicePort.getCustomerById(1L)).thenReturn(customer);
        when(productServicePort.getProductById(100L)).thenReturn(product);
        when(stockServicePort.hasSufficientStock(100L, 2)).thenReturn(true);

        Sale saleSaved = Sale.builder()
                .id(10L)
                .customerId(1L)
                .totalAmount(3000.0)
                .status(SaleStatus.CONFIRMED)
                .createdAt(LocalDateTime.now())
                .items(List.of(
                        SaleItem.builder().productId(100L).quantity(2).price(1500.0).productName("Laptop").build()
                ))
                .build();

        when(saleRepositoryPort.save(any(Sale.class))).thenReturn(saleSaved);

        // Act
        SaleResponse response = registerSaleService.registerSale(request);

        // Assert
        assertNotNull(response);
        assertEquals(10L, response.getSaleId());
        assertEquals(3000.0, response.getTotalAmount());
        assertEquals(SaleStatus.CONFIRMED.name(), response.getStatus());

        verify(stockServicePort, times(1)).decreaseStock(100L, 2);
        verify(saleEventPublisher, times(1)).publishSaleCreated(any(SaleCreatedEvent.class));
    }

    @Test
    void testRegisterSale_insufficientStock() {
        // Arrange
        CreateSaleRequest request = CreateSaleRequest.builder()
                .customerId(1L)
                .items(List.of(
                        SaleItemRequest.builder().productId(200L).quantity(5).build()
                ))
                .build();

        CustomerResponse customer = new CustomerResponse();
        customer.setId(1L);
        customer.setFirstName("Ana");
        customer.setLastName("Gómez");
        customer.setEmail("ana@test.com");
        ProductResponse product = new ProductResponse();
        product.setId(200L);
        product.setName("Mouse");
        product.setPrice(50.0);

        when(customerServicePort.getCustomerById(1L)).thenReturn(customer);
        when(productServicePort.getProductById(200L)).thenReturn(product);
        when(stockServicePort.hasSufficientStock(200L, 5)).thenReturn(false);

        // Act & Assert
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> registerSaleService.registerSale(request));

        assertEquals("Stock insuficiente para el producto Mouse", ex.getMessage());

        verify(saleRepositoryPort, never()).save(any());
        verify(saleEventPublisher, never()).publishSaleCreated(any());
    }
}
