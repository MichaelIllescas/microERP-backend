// application/usecase/GetCategoryByIdService.java
package com.imperialnet.product_service.application.usecase;

import com.imperialnet.product_service.application.dto.CategoryResponse;
import com.imperialnet.product_service.application.port.in.GetCategoryByIdUseCase;
import com.imperialnet.product_service.application.port.out.CategoryRepositoryPort;
import com.imperialnet.product_service.infrastructure.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetCategoryByIdService implements GetCategoryByIdUseCase {

    private final CategoryRepositoryPort categoryRepositoryPort;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse getById(Long id) {
        log.info("Fetching category with id={}", id);
        return categoryRepositoryPort.findById(id)
                .map(categoryMapper::toResponse)
                .orElseThrow(() -> {
                    log.warn("Category with id={} not found", id);
                    return new IllegalArgumentException("Categoría no encontrada con id " + id);
                });
    }
}
