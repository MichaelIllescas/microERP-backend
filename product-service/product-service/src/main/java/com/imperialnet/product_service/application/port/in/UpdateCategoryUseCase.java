// application/port/in/UpdateCategoryUseCase.java
package com.imperialnet.product_service.application.port.in;

import com.imperialnet.product_service.application.dto.CategoryResponse;
import com.imperialnet.product_service.application.dto.UpdateCategoryRequest;

public interface UpdateCategoryUseCase {
    CategoryResponse update(Long id, UpdateCategoryRequest request);
}
