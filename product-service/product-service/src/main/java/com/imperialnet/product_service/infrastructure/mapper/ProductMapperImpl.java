package com.imperialnet.product_service.infrastructure.mapper;

import com.imperialnet.product_service.domain.model.Category;
import com.imperialnet.product_service.domain.model.Product;
import com.imperialnet.product_service.application.dto.CreateProductRequest;
import com.imperialnet.product_service.application.dto.ProductResponse;
import com.imperialnet.product_service.application.dto.CategoryResponse;
import com.imperialnet.product_service.infrastructure.persistence.entity.CategoryEntity;
import com.imperialnet.product_service.infrastructure.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public Product toDomain(CreateProductRequest request) {
        if (request == null) return null;
        return Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(Category.builder()
                        .id(request.getCategoryId())
                        .build())
                .build();
    }

    @Override
    public ProductEntity toEntity(Product product) {
        if (product == null) return null;
        return ProductEntity.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .category(toEntity(product.getCategory()))
                .build();
    }

    @Override
    public Product toDomain(ProductEntity entity) {
        if (entity == null) return null;
        return Product.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .category(toDomain(entity.getCategory()))
                .build();
    }

    @Override
    public Category toDomain(CategoryEntity entity) {
        if (entity == null) return null;
        return Category.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }

    @Override
    public CategoryEntity toEntity(Category category) {
        if (category == null) return null;
        return CategoryEntity.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    @Override
    public ProductResponse toResponse(Product product) {
        if (product == null) return null;
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .category(toResponse(product.getCategory()))
                .build();
    }

    @Override
    public CategoryResponse toResponse(Category category) {
        if (category == null) return null;
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }
}
