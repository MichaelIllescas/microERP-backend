package com.imperialnet.product_service.application.usecase;

import com.imperialnet.product_service.application.dto.CategoryResponse;
import com.imperialnet.product_service.application.dto.UpdateCategoryRequest;
import com.imperialnet.product_service.application.port.out.CategoryRepositoryPort;
import com.imperialnet.product_service.domain.model.Category;
import com.imperialnet.product_service.infrastructure.mapper.CategoryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateCategoryServiceTest {

    private CategoryRepositoryPort categoryRepositoryPort;
    private CategoryMapper categoryMapper;
    private UpdateCategoryService updateCategoryService;

    @BeforeEach
    void setUp() {
        categoryRepositoryPort = Mockito.mock(CategoryRepositoryPort.class);
        categoryMapper = Mockito.mock(CategoryMapper.class);
        updateCategoryService = new UpdateCategoryService(categoryRepositoryPort, categoryMapper);
    }

    @Test
    void shouldUpdateCategorySuccessfully() {
        Long categoryId = 1L;
        Category existing = Category.builder()
                .id(categoryId)
                .name("Old name")
                .description("Old desc")
                .build();

        UpdateCategoryRequest request = UpdateCategoryRequest.builder()
                .name("New name")
                .description("New desc")
                .build();

        Category updated = existing.toBuilder()
                .name("New name")
                .description("New desc")
                .build();

        CategoryResponse expectedResponse = CategoryResponse.builder()
                .id(categoryId)
                .name("New name")
                .description("New desc")
                .build();

        when(categoryRepositoryPort.findById(categoryId)).thenReturn(Optional.of(existing));
        when(categoryRepositoryPort.save(existing)).thenReturn(updated);
        when(categoryMapper.toResponse(updated)).thenReturn(expectedResponse);

        CategoryResponse result = updateCategoryService.update(categoryId, request);

        assertEquals("New name", result.getName());
        assertEquals("New desc", result.getDescription());
        verify(categoryRepositoryPort, times(1)).save(existing);
    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFound() {
        Long categoryId = 99L;
        UpdateCategoryRequest request = UpdateCategoryRequest.builder()
                .name("New")
                .description("Desc")
                .build();

        when(categoryRepositoryPort.findById(categoryId)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> updateCategoryService.update(categoryId, request));

        assertEquals("Categoría no encontrada con id 99", ex.getMessage());
    }
}
