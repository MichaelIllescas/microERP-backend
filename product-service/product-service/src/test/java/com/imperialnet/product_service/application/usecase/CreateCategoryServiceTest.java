package com.imperialnet.product_service.application.usecase;

import com.imperialnet.product_service.application.dto.CategoryResponse;
import com.imperialnet.product_service.application.dto.CreateCategoryRequest;
import com.imperialnet.product_service.application.port.out.CategoryRepositoryPort;
import com.imperialnet.product_service.domain.model.Category;
import com.imperialnet.product_service.infrastructure.mapper.CategoryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateCategoryServiceTest {

    @Mock
    private CategoryRepositoryPort repositoryPort;

    @Mock
    private CategoryMapper mapper;

    @InjectMocks
    private CreateCategoryService service;

    private CreateCategoryRequest request;
    private Category category;
    private Category saved;
    private CategoryResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        request = CreateCategoryRequest.builder()
                .name("Electrónica")
                .description("Dispositivos y accesorios")
                .build();

        category = Category.builder()
                .id(null)
                .name("Electrónica")
                .description("Dispositivos y accesorios")
                .build();

        saved = Category.builder()
                .id(1L)
                .name("Electrónica")
                .description("Dispositivos y accesorios")
                .build();

        response = CategoryResponse.builder()
                .id(1L)
                .name("Electrónica")
                .description("Dispositivos y accesorios")
                .build();
    }

    @Test
    void shouldCreateCategorySuccessfully_whenNameDoesNotExist() {
        // given
        when(repositoryPort.existsByName(request.getName())).thenReturn(false);
        when(mapper.toDomain(request)).thenReturn(category);
        when(repositoryPort.save(category)).thenReturn(saved);
        when(mapper.toResponse(saved)).thenReturn(response);

        // when
        CategoryResponse result = service.createCategory(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Electrónica");

        verify(repositoryPort).existsByName("Electrónica");
        verify(repositoryPort).save(category);
        verify(mapper).toResponse(saved);
    }

    @Test
    void shouldThrowException_whenCategoryAlreadyExists() {
        // given
        when(repositoryPort.existsByName(request.getName())).thenReturn(true);

        // when & then
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.createCategory(request)
        );

        assertThat(ex.getMessage()).isEqualTo("La categoría ya existe");

        verify(repositoryPort).existsByName("Electrónica");
        verify(repositoryPort, never()).save(any(Category.class));
    }
}
