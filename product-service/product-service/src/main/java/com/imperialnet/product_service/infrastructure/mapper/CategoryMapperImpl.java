package com.imperialnet.product_service.infrastructure.mapper;



import com.imperialnet.product_service.application.dto.CategoryResponse;
import com.imperialnet.product_service.application.dto.CreateCategoryRequest;
import com.imperialnet.product_service.domain.model.Category;
import com.imperialnet.product_service.infrastructure.persistence.entity.CategoryEntity;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapperImpl implements CategoryMapper {

    // Request → Dominio
    public Category toDomain(CreateCategoryRequest request) {
        if (request == null) return null;
        return Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }

    // Dominio → Response
    public CategoryResponse toResponse(Category category) {
        if (category == null) return null;
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    // Dominio → Entity
    public CategoryEntity toEntity(Category category) {
        if (category == null) return null;
        return CategoryEntity.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    // Entity → Dominio
    public Category toDomain(CategoryEntity entity) {
        if (entity == null) return null;
        return Category.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }
}
