package com.imperialnet.product_service.application.usecase;

import com.imperialnet.product_service.application.port.out.CategoryRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static com.google.common.base.CharMatcher.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class DeleteCategoryServiceTest {

    @Mock
    private CategoryRepositoryPort categoryRepositoryPort;
     @InjectMocks
    private DeleteCategoryService deleteCategoryService;

    @Test
    void shouldDeleteCategorySuccessfully() {
        // given
        Long categoryId = 1L;
        when(categoryRepositoryPort.existsById(categoryId)).thenReturn(true);

        // when
        deleteCategoryService.deleteById(categoryId);

        // then
        verify( categoryRepositoryPort, times(1)).deleteById(categoryId);
    }

    @Test
    void shouldThrowExceptionWhenCategoryDoesNotExist() {
        // given
        Long categoryId = 99L;
        when(categoryRepositoryPort.existsById(categoryId)).thenReturn(false);

        // when & then
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> deleteCategoryService.deleteById(categoryId));

        assertEquals("Categoría no encontrada con id 99", exception.getMessage());
        verify(categoryRepositoryPort, never()).deleteById(categoryId);
    }

    @Test
    void shouldThrowExceptionWhenCategoryIsInUse() {
        // given
        Long categoryId = 2L;
        when(categoryRepositoryPort.existsById(categoryId)).thenReturn(true);
        doThrow(new DataIntegrityViolationException("FK violation"))
                .when(categoryRepositoryPort).deleteById(categoryId);

        // when & then
        IllegalStateException exception =
                assertThrows(IllegalStateException.class,
                        () -> deleteCategoryService.deleteById(categoryId));

        assertEquals("No se puede eliminar la categoría porque está asociada a productos.", exception.getMessage());
        verify(categoryRepositoryPort, times(1)).deleteById(categoryId);
    }
}