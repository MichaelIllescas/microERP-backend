package com.imperialnet.product_service.application.usecase;

import com.imperialnet.product_service.application.port.out.ProductRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteProductServiceTest {

    private ProductRepositoryPort productRepositoryPort;
    private DeleteProductService deleteProductService;

    @BeforeEach
    void setUp() {
        productRepositoryPort = Mockito.mock(ProductRepositoryPort.class);
        deleteProductService = new DeleteProductService(productRepositoryPort);
    }

    @Test
    void shouldDeleteProductSuccessfully() {
        // given
        Long productId = 1L;
        when(productRepositoryPort.categoryExists(productId)).thenReturn(true);

        // when
        deleteProductService.deleteById(productId);

        // then
        verify(productRepositoryPort, times(1)).deleteById(productId);
    }

    @Test
    void shouldThrowExceptionWhenProductDoesNotExist() {
        // given
        Long productId = 99L;
        when(productRepositoryPort.categoryExists(productId)).thenReturn(false);

        // when & then
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> deleteProductService.deleteById(productId));

        assertEquals("Producto no encontrado con id 99", exception.getMessage());
        verify(productRepositoryPort, never()).deleteById(any());
    }

    @Test
    void shouldThrowExceptionWhenProductIsInUse() {
        // given
        Long productId = 2L;
        when(productRepositoryPort.categoryExists(productId)).thenReturn(true);
        doThrow(new DataIntegrityViolationException("FK violation"))
                .when(productRepositoryPort).deleteById(productId);

        // when & then
        IllegalStateException exception =
                assertThrows(IllegalStateException.class,
                        () -> deleteProductService.deleteById(productId));

        assertEquals("No se puede eliminar el producto porque está asociado a otros registros.", exception.getMessage());
        verify(productRepositoryPort, times(1)).deleteById(productId);
    }
}
