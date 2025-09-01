package com.imperialnet.product_service.application.usecase;

import com.imperialnet.product_service.application.dto.CategoryResponse;
import com.imperialnet.product_service.application.dto.CreateCategoryRequest;
import com.imperialnet.product_service.application.port.in.CreateCategoryUseCase;
import com.imperialnet.product_service.application.port.out.CategoryRepositoryPort;
import com.imperialnet.product_service.domain.model.Category;
import com.imperialnet.product_service.infrastructure.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateCategoryService implements CreateCategoryUseCase {

    private final CategoryRepositoryPort repositoryPort;
    private final CategoryMapper mapper;

    @Override
    @Transactional
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        log.info("▶️ Creando categoría: {}", request);

        if (repositoryPort.existsByName(request.getName())) {
            log.error("❌ Ya existe una categoría con nombre {}", request.getName());
            throw new IllegalArgumentException("La categoría ya existe");
        }

        Category category = mapper.toDomain(request);
        Category saved = repositoryPort.save(category);
        log.info("✅ Categoría guardada con ID: {}", saved.getId());

        return mapper.toResponse(saved);
    }
}
