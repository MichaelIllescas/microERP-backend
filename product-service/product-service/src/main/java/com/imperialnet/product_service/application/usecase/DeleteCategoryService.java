// application/usecase/DeleteCategoryService.java
package com.imperialnet.product_service.application.usecase;

import com.imperialnet.product_service.application.port.in.DeleteCategoryUseCase;
import com.imperialnet.product_service.application.port.out.CategoryRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteCategoryService implements DeleteCategoryUseCase {

    private final CategoryRepositoryPort categoryRepositoryPort;

    @Override
    public void deleteById(Long id) {
        log.info("Attempting to delete category with id={}", id);

        if (!categoryRepositoryPort.existsById(id)) {
            log.warn("Category with id={} not found", id);
            throw new IllegalArgumentException("Categoría no encontrada con id " + id);
        }

        try {
            categoryRepositoryPort.deleteById(id);
            log.info("Successfully deleted category with id={}", id);
        } catch (DataIntegrityViolationException e) {
            log.error("Cannot delete category with id={} because it is being used by products", id, e);
            throw new IllegalStateException("No se puede eliminar la categoría porque está asociada a productos.");
        }
    }
}
