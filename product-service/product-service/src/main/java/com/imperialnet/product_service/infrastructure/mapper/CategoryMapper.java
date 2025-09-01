package com.imperialnet.product_service.infrastructure.mapper;

import com.imperialnet.product_service.application.dto.CategoryResponse;
import com.imperialnet.product_service.application.dto.CreateCategoryRequest;
import com.imperialnet.product_service.domain.model.Category;
import com.imperialnet.product_service.infrastructure.persistence.entity.CategoryEntity;


public interface CategoryMapper {
    Category toDomain(CreateCategoryRequest request);
    CategoryResponse toResponse(Category category);

    CategoryEntity toEntity(Category category);
    Category toDomain(CategoryEntity entity);
}
