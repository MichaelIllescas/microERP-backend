package com.imperialnet.product_service.application.usecase;

import com.imperialnet.product_service.application.dto.ProductDeletedEvent;
import com.imperialnet.product_service.application.event.ProductEventPublisher;
import com.imperialnet.product_service.application.port.out.ProductRepositoryPort;
import com.imperialnet.product_service.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteProductServiceTest {

    private ProductRepositoryPort productRepositoryPort;
    private ProductEventPublisher eventPublisher;
    private DeleteProductService deleteProductService;

    @BeforeEach
    void setUp() {
        productRepositoryPort = mock(ProductRepositoryPort.class);
        eventPublisher = mock(ProductEventPublisher.class);
        deleteProductService = new DeleteProductService(productRepositoryPort, eventPublisher);
    }

    @Test
    @DisplayName("✅ Debería eliminar el producto y publicar evento cuando existe")
    void shouldDeleteProductSuccessfullyAndPublishEvent() {
        Long productId = 1L;
        Product dummyProduct = Product.builder().id(productId).build();
        when(productRepositoryPort.findById(productId)).thenReturn(Optional.of(dummyProduct));

        deleteProductService.deleteById(productId);

        verify(productRepositoryPort, times(1)).deleteById(productId);

        // Capturar y verificar evento publicado
        ArgumentCaptor<ProductDeletedEvent> captor = ArgumentCaptor.forClass(ProductDeletedEvent.class);
        verify(eventPublisher, times(1)).publishProductDeleted(captor.capture());

        assertEquals(productId, captor.getValue().getProductId());
    }

    @Test
    @DisplayName("❌ Debería lanzar excepción si el producto no existe")
    void shouldThrowExceptionWhenProductDoesNotExist() {
        Long productId = 99L;
        when(productRepositoryPort.findById(productId)).thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> deleteProductService.deleteById(productId));

        assertEquals("Producto no encontrado con id 99", exception.getMessage());
        verify(productRepositoryPort, never()).deleteById(any());
        verify(eventPublisher, never()).publishProductDeleted(any());
    }

    @Test
    @DisplayName("⚠️ Debería lanzar excepción si el producto está en uso en otros registros")
    void shouldThrowExceptionWhenProductIsInUse() {
        Long productId = 2L;
        Product dummyProduct = Product.builder().id(productId).build();
        when(productRepositoryPort.findById(productId)).thenReturn(Optional.of(dummyProduct));
        doThrow(new org.springframework.dao.DataIntegrityViolationException("FK violation"))
                .when(productRepositoryPort).deleteById(productId);

        IllegalStateException exception =
                assertThrows(IllegalStateException.class,
                        () -> deleteProductService.deleteById(productId));

        assertEquals("No se puede eliminar el producto porque está asociado a otros registros.", exception.getMessage());
        verify(productRepositoryPort, times(1)).deleteById(productId);
        verify(eventPublisher, never()).publishProductDeleted(any());
    }
}
