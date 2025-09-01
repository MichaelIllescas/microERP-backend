// application/usecase/UpdateCategoryService.java
package com.imperialnet.product_service.application.usecase;

import com.imperialnet.product_service.application.dto.CategoryResponse;
import com.imperialnet.product_service.application.dto.UpdateCategoryRequest;
import com.imperialnet.product_service.application.port.in.UpdateCategoryUseCase;
import com.imperialnet.product_service.application.port.out.CategoryRepositoryPort;
import com.imperialnet.product_service.domain.model.Category;
import com.imperialnet.product_service.infrastructure.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateCategoryService implements UpdateCategoryUseCase {

    private final CategoryRepositoryPort categoryRepositoryPort;
    private final CategoryMapper mapper;

    @Override
    public CategoryResponse update(Long id, UpdateCategoryRequest request) {
        log.info("Attempting to update category with id={}", id);

        Category existing = categoryRepositoryPort.findById(id)
                .orElseThrow(() -> {
                    log.warn("Category with id={} not found", id);
                    return new IllegalArgumentException("Categoría no encontrada con id " + id);
                });

        // ✅ solo actualizar si los campos no son null
        if (request.getName() != null) {
            existing.setName(request.getName());
        }
        if (request.getDescription() != null) {
            existing.setDescription(request.getDescription());
        }

        Category updated = categoryRepositoryPort.save(existing);
        log.info("Successfully updated category with id={}", id);

        return mapper.toResponse(updated);
    }
}
