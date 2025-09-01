package com.imperialnet.product_service.application.usecase;

import com.imperialnet.product_service.application.dto.CategoryResponse;
import com.imperialnet.product_service.application.dto.CreateProductRequest;
import com.imperialnet.product_service.application.dto.ProductResponse;
import com.imperialnet.product_service.application.port.out.CategoryRepositoryPort;
import com.imperialnet.product_service.application.port.out.ProductRepositoryPort;
import com.imperialnet.product_service.domain.model.Category;
import com.imperialnet.product_service.domain.model.Product;
import com.imperialnet.product_service.infrastructure.mapper.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateProductServiceTest {

    @Mock
    private ProductRepositoryPort repositoryPort;

    @Mock
    private CategoryRepositoryPort categoryRepositoryPort;

    @Mock
    private ProductMapper mapper;

    @InjectMocks
    private CreateProductService service;

    private CreateProductRequest request;
    private Product product;
    private Product saved;
    private ProductResponse response;
    private Category category;
    private CategoryResponse categoryResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        request = new CreateProductRequest();
        request.setName("Laptop");
        request.setDescription("Laptop de alta gama");
        request.setPrice(1500.0);
        request.setCategoryId(1L);

        category = Category.builder()
                .id(1L)
                .name("Electrónica")
                .description("Dispositivos electrónicos")
                .build();

        categoryResponse = CategoryResponse.builder()
                .id(1L)
                .name("Electrónica")
                .description("Dispositivos electrónicos")
                .build();

        product = Product.builder()
                .id(null)
                .name("Laptop")
                .description("Laptop de alta gama")
                .price(1500.0)
                .category(category)
                .build();

        saved = Product.builder()
                .id(100L)
                .name("Laptop")
                .description("Laptop de alta gama")
                .price(1500.0)
                .category(category)
                .build();

        response = ProductResponse.builder()
                .id(100L)
                .name("Laptop")
                .description("Laptop de alta gama")
                .price(1500.0)
                .category(categoryResponse)
                .build();
    }

    @Test
    void shouldCreateProductSuccessfully_whenCategoryExists() {
        // given
        when(categoryRepositoryPort.existsById(request.getCategoryId())).thenReturn(true);
        when(mapper.toDomain(request)).thenReturn(product);
        when(repositoryPort.save(any(Product.class))).thenReturn(saved);
        when(mapper.toResponse(saved)).thenReturn(response);

        // when
        ProductResponse result = service.createProduct(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(100L);
        assertThat(result.getName()).isEqualTo("Laptop");
        assertThat(result.getCategory().getName()).isEqualTo("Electrónica");

        verify(categoryRepositoryPort).existsById(1L);
        verify(repositoryPort).save(any(Product.class));
        verify(mapper).toResponse(saved);
    }

    @Test
    void shouldThrowException_whenCategoryDoesNotExist() {
        // given
        when(categoryRepositoryPort.existsById(request.getCategoryId())).thenReturn(false);

        // when & then
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.createProduct(request)
        );

        assertThat(ex.getMessage()).isEqualTo("La categoría no existe");

        verify(categoryRepositoryPort).existsById(1L);
        verify(repositoryPort, never()).save(any(Product.class));
    }

}
