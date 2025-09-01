// application/usecase/ListCategoriesService.java
package com.imperialnet.product_service.application.usecase;

import com.imperialnet.product_service.application.dto.CategoryResponse;
import com.imperialnet.product_service.application.port.in.ListCategoriesUseCase;
import com.imperialnet.product_service.application.port.out.CategoryRepositoryPort;
import com.imperialnet.product_service.infrastructure.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ListCategoriesService implements ListCategoriesUseCase {

    private final CategoryRepositoryPort categoryRepositoryPort;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponse> getAllCategories() {
        log.info("Fetching all categories from repository...");

        List<CategoryResponse> categories = categoryRepositoryPort.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();

        log.debug("Mapped {} categories to response DTOs", categories.size());
        log.info("Successfully retrieved {} categories", categories.size());

        return categories;
    }
}
