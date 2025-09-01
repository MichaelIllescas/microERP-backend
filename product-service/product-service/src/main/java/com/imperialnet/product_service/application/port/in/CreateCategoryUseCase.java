package com.imperialnet.product_service.application.port.in;

import com.imperialnet.product_service.application.dto.CategoryResponse;
import com.imperialnet.product_service.application.dto.CreateCategoryRequest;

public interface CreateCategoryUseCase {
    CategoryResponse createCategory(CreateCategoryRequest request);
}
