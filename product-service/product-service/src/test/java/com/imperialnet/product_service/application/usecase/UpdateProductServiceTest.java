package com.imperialnet.product_service.application.usecase;

import com.imperialnet.product_service.application.dto.ProductResponse;
import com.imperialnet.product_service.application.dto.UpdateProductRequest;
import com.imperialnet.product_service.application.port.out.CategoryRepositoryPort;
import com.imperialnet.product_service.application.port.out.ProductRepositoryPort;
import com.imperialnet.product_service.domain.model.Category;
import com.imperialnet.product_service.domain.model.Product;
import com.imperialnet.product_service.infrastructure.mapper.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateProductServiceTest {

    private ProductRepositoryPort productRepositoryPort;
    private CategoryRepositoryPort categoryRepositoryPort;
    private ProductMapper productMapper;
    private UpdateProductService updateProductService;

    @BeforeEach
    void setUp() {
        productRepositoryPort = Mockito.mock(ProductRepositoryPort.class);
        categoryRepositoryPort = Mockito.mock(CategoryRepositoryPort.class);
        productMapper = Mockito.mock(ProductMapper.class);
        updateProductService = new UpdateProductService(productRepositoryPort, categoryRepositoryPort, productMapper);
    }

    @Test
    void shouldUpdateProductSuccessfully() {
        Long productId = 1L;
        Long categoryId = 10L;

        Product existing = Product.builder()
                .id(productId)
                .name("Old name")
                .description("Old desc")
                .price(100.0)
                .category(Category.builder().id(categoryId).build())
                .build();

        UpdateProductRequest request = UpdateProductRequest.builder()
                .name("New name")
                .description("New desc")
                .price(200.0)
                .categoryId(categoryId)
                .build();

        Product updated = existing.toBuilder()
                .name("New name")
                .description("New desc")
                .price(200.0)
                .build();

        ProductResponse expectedResponse = ProductResponse.builder()
                .id(productId)
                .name("New name")
                .description("New desc")
                .price(200.0)
                .category(null)
                .build();

        when(productRepositoryPort.findById(productId)).thenReturn(Optional.of(existing));
        when(categoryRepositoryPort.existsById(categoryId)).thenReturn(true);
        when(productRepositoryPort.save(existing)).thenReturn(updated);
        when(productMapper.toResponse(updated)).thenReturn(expectedResponse);

        ProductResponse result = updateProductService.update(productId, request);

        assertEquals("New name", result.getName());
        assertEquals(200.0, result.getPrice());
        verify(productRepositoryPort, times(1)).save(existing);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        Long productId = 99L;
        UpdateProductRequest request = UpdateProductRequest.builder()
                .name("New name")
                .description("New desc")
                .price(200.0)
                .categoryId(10L)
                .build();

        when(productRepositoryPort.findById(productId)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> updateProductService.update(productId, request));

        assertEquals("Producto no encontrado con id 99", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFound() {
        Long productId = 1L;
        Long categoryId = 10L;

        Product existing = Product.builder().id(productId).name("Old").build();
        UpdateProductRequest request = UpdateProductRequest.builder()
                .name("New")
                .description("Desc")
                .price(100.0)
                .categoryId(categoryId)
                .build();

        when(productRepositoryPort.findById(productId)).thenReturn(Optional.of(existing));
        when(categoryRepositoryPort.existsById(categoryId)).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> updateProductService.update(productId, request));

        assertEquals("Categoría no encontrada con id 10", ex.getMessage());
    }
}
